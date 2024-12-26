package edu.tamu.scholars.discovery.defaults;

import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.VERSION;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.model.Named;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;

public abstract class AbstractDefaults<E extends Named, R extends NamedRepo<E>> implements Defaults<E> {

    private static final String CREATED_DEFAULTS = "Created {} {} defaults.";

    private static final String UPDATED_DEFAULTS = "Updated {} {} defaults.";

    protected static final String CLASSPATH = "classpath:%s";

    protected static final String IO_EXCEPTION_MESSAGE = "Could not read %s. Either does not exists or is not a file.";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final ObjectMapper mapper;

    private final MiddlewareConfig config;

    protected final ResourcePatternResolver resolver;

    protected final R repo;

    protected AbstractDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            R repo) {
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.config = config;
        this.resolver = resolver;
        this.repo = repo;
    }

    @Override
    public void load() throws IOException {
        Resource[] resources = resolver.getResources(path());
        List<E> entities = read(resources);
        for (E entity : entities) {
            process(entity);
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
        if (config.isUpdateDefaults()) {
            copyProperties(entity, existingEntity);
            repo.save(existingEntity);
            logger.info(UPDATED_DEFAULTS, this.getClass().getSimpleName(), entity.getName());
        }
    }

    protected <N extends Named> List<N> loadResources(Resource[] resources, Class<N> type) throws IOException {
        List<N> entities = new ArrayList<>();
        for (Resource resource : resources) {
            if (resource.exists() && resource.isFile()) {
                entities.add(mapper.readValue(resource.getInputStream(), type));
            }
        }

        return entities;
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

    protected void copyProperties(E source, E target) {
        BeanUtils.copyProperties(source, target, ID, VERSION);
    }

}
