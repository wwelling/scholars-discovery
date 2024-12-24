package edu.tamu.scholars.discovery.defaults;

import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.ID;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.model.Named;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;


public abstract class AbstractDefaults<E extends Named, R extends NamedRepo<E>> implements Defaults<E, R> {

    private static final String CREATED_DEFAULTS = "Created {} {} defaults.";

    private static final String UPDATED_DEFAULTS = "Updated {} {} defaults.";

    protected static final String CLASSPATH = "classpath:%s";

    protected static final String IO_EXCEPTION_MESSAGE = "Could not read %s. Either does not exists or is not a file.";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final ObjectMapper mapper;

    @Autowired
    private MiddlewareConfig discovery;

    @Autowired
    protected ResourcePatternResolver resolver;

    @Autowired
    protected R repo;

    public AbstractDefaults() {
        mapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public void load() throws IOException {
        Resource resource = resolver.getResource(path());
        if (resource.exists()) {
            List<E> entities = read(resource.getInputStream());
            for (E entity : entities) {
                process(entity);
            }
        }
    }

    @Override
    public void process(E entity) {
        Optional<E> existingEntity = repo.findByName(entity.getName());
        if (existingEntity.isPresent()) {
            update(entity, existingEntity.get());
        } else {
            repo.save(entity);
            logger.info(CREATED_DEFAULTS, this.getClass().getSimpleName(), entity.getName());
        }
    }

    @Override
    public void update(E entity, E existingEntity) {
        if (discovery.isUpdateDefaults()) {
            BeanUtils.copyProperties(entity, existingEntity, ID);
            repo.save(existingEntity);
            logger.info(UPDATED_DEFAULTS, this.getClass().getSimpleName(), entity.getName());
        }
    }

    protected void loadTemplateMap(Map<String, String> templateMap) throws IOException {
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            String path = entry.getValue();
            Resource resource = resolver.getResource(String.format(CLASSPATH, path));
            if (resource.exists()) {
                entry.setValue(IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8));
            } else {
                throw new IOException(String.format(IO_EXCEPTION_MESSAGE, path));
            }
        }
    }

}
