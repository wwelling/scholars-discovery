package edu.tamu.scholars.discovery.etl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.jena.rdf.model.Property;

public class EtlCacheUtility {

    private static final int PROPERTY_CACHE_INITIAL_CAPACITY = 100;
    private static final int VALUES_CACHE_INITIAL_CAPACITY = 7000;

    private static final Map<String, Property> PROPERTY_CACHE = new ConcurrentHashMap<>(
        PROPERTY_CACHE_INITIAL_CAPACITY);

    private static final Map<String, List<String>> VALUES_CACHE = new ConcurrentHashMap<>(
        VALUES_CACHE_INITIAL_CAPACITY);

    private EtlCacheUtility() {

    }

    public static Property computePropertyIfAbsent(String key, Function<String, Property> mappingFunction) {
        return computeIfAbsent(PROPERTY_CACHE, key, mappingFunction);
    }

    public static List<String> computeValuesIfAbsent(String key, Function<String, List<String>> mappingFunction) {
        return computeIfAbsent(VALUES_CACHE, key, mappingFunction);
    }

    public static void clearCache() {
        clearPropertyCache();
        clearValuesCache();
    }

    public static void clearPropertyCache() {
        PROPERTY_CACHE.clear();
    }

    public static void clearValuesCache() {
        VALUES_CACHE.clear();
    }

    private static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<K, V> mappingFunction) {
        V values = map.get(key);
        if (values == null) {
            values = mappingFunction.apply(key);
            if (values != null) {
                map.put(key, values);
            }
        }

        return values;
    }

}
