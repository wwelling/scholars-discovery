package edu.tamu.scholars.discovery.component;

public interface Mapper<T> extends Service {

    public T valueToTree(Object value);

}
