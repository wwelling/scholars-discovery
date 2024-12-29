package edu.tamu.scholars.discovery.config.model;

import edu.tamu.scholars.discovery.component.Service;

public abstract class ComponentConfig<S extends Service> {

    public abstract Class<? extends S> getType();

}
