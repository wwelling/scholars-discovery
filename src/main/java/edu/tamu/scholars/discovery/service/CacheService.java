package edu.tamu.scholars.discovery.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * A cache service autowired in {@see edu.tamu.scholars.discovery.discovery.component.TriplestoreHarvester}.
 */
@Service
public class CacheService {

    private static final int MAX_CAPACITY = 7000;

    private final Map<String, List<String>> cache;

    public CacheService() {
        cache = new ConcurrentHashMap<>(MAX_CAPACITY);
    }

    /**
     * Get list of values or null if not found.
     * 
     * @param key key to lookup in cache
     * @return See the {@see java.util.concurrent.ConcurrentHashMap#get}
     */
    public List<String> get(String key) {
        return cache.get(key);
    }

    /**
     * Put into the cache and return previous list of values for key.
     * 
     * @param key key to lookup in cache
     * @param values current values for key to store
     * @return See the {@see java.util.concurrent.ConcurrentHashMap#put}
     */
    public List<String> put(String key, List<String> values) {
        return cache.put(key, values);
    }

}
