package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

import edu.tamu.scholars.discovery.model.Named;

public interface Defaults<E extends Named> {

    public String path();

    public List<E> read(Resource[] resources) throws IOException;

    public void load() throws IOException;

    public void process(E entity);

    public void update(E entity, E existingEntity);

    public List<Class<?>> getDefaultDependencies();

}
