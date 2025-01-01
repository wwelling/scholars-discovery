package edu.tamu.scholars.discovery.etl.extract;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.jena.rdf.model.Property;

public class ExtractorCacheUtility {

    private static final int PROPERTY_CACHE_INITIAL_CAPACITY = 100;
    private static final int VALUES_CACHE_INITIAL_CAPACITY = 7000;

    public static final Map<String, Property> PROPERTY_CACHE = new ConcurrentHashMap<>(
            PROPERTY_CACHE_INITIAL_CAPACITY);

    public static final Map<String, List<String>> VALUES_CACHE = new ConcurrentHashMap<>(
            VALUES_CACHE_INITIAL_CAPACITY);

    private ExtractorCacheUtility() {

    }

}
