package edu.tamu.scholars.discovery.config;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import edu.tamu.scholars.discovery.service.ResourceService;

/**
 * Configuration for caching behaviors.
 * 
 * <p>Enables {@link Cacheable} annotation used in {@link ResourceService}.</p>
 */
@EnableCaching
@Configuration
public class CacheConfig {

}