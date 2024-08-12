package edu.tamu.scholars.middleware.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * A cache service autowired in {@see edu.tamu.scholars.middleware.discovery.component.TriplestoreHarvester}.
 */
@Service
public class CacheService {

    private static final int MAX_CAPACITY = 7000;

    private final Map<String, List<Object>> cache;

    public CacheService() {
        cache = new ConcurrentHashMap<>(MAX_CAPACITY);
    }

    /**
     * Get list of values or null if not found.
     * 
     * @param key key to lookup in cache
     * @return See the {@see java.util.concurrent.ConcurrentHashMap#get}
     */
    public List<Object> get(String key) {
        return cache.get(key);
    }

    /**
     * Put into the cache and return previous list of values for key.
     * 
     * @param key key to lookup in cache
     * @param values current values for key to store
     * @return See the {@see java.util.concurrent.ConcurrentHashMap#put}
     */
    public List<Object> put(String key, List<Object> values) {
        return cache.put(key, values);
    }

}
