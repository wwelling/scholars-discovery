package edu.tamu.scholars.discovery.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private static final int MAX_CAPACITY = 7000;

    private final Map<String, List<String>> cache;

    public CacheService() {
        cache = new ConcurrentHashMap<>(MAX_CAPACITY);
    }

    public List<String> get(String key) {
        return cache.get(key);
    }

    public List<String> put(String key, List<String> values) {
        return cache.put(key, values);
    }

}
