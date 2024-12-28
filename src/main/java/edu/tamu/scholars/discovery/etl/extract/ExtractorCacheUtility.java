package edu.tamu.scholars.discovery.etl.extract;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ExtractorCacheUtility {

    private static final int MAX_CAPACITY = 2000;

    private static final Map<String, List<String>> CACHE = new ConcurrentHashMap<>(MAX_CAPACITY);

    private ExtractorCacheUtility() {

    }

    public static void clear() {
        CACHE.clear();
    }

    public static List<String> get(String key) {
        return CACHE.get(key);
    }

    public static List<String> put(String key, List<String> values) {
        return CACHE.put(key, values);
    }

}
