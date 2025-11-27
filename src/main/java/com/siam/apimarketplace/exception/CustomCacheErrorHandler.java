package com.siam.apimarketplace.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Redis GET failed for cache: {} key: {}. Falling back to database.", cache.getName(), key);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn("Redis PUT failed for cache: {} key: {}. Continuing without cache.", cache.getName(), key);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Redis EVICT failed for cache: {} key: {}. Continuing without cache.", cache.getName(), key);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn("Redis CLEAR failed for cache: {}. Continuing without cache.", cache.getName());
    }
}
