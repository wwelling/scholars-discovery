package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.tamu.scholars.discovery.model.Named;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;


public interface Defaults<E extends Named, R extends NamedRepo<E>> {

    public String path();

    public List<E> read(InputStream is) throws IOException;

    public void load() throws IOException;

    public void process(E entity);

    public void update(E entity, E existingEntity);

}
