package com.siam.apimarketplace.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache Management Controller
 * Provides endpoints to monitor and clear caches manually
 * Useful for debugging and maintenance
 */
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
@Slf4j
public class CacheController {

    private final CacheManager cacheManager;

    /**
     * Clear a specific cache by name
     *
     * Example: DELETE /api/cache/products
     * This will clear all cached individual products
     */
    @DeleteMapping("/{cacheName}")
    public ResponseEntity<Map<String, String>> clearCache(@PathVariable String cacheName) {
        log.info("Request to clear cache: {}", cacheName);

        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            log.info("Cache '{}' cleared successfully", cacheName);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Cache '" + cacheName + "' cleared successfully");
            response.put("status", "success");

            return ResponseEntity.ok(response);
        }

        log.warn("Cache '{}' not found", cacheName);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache '" + cacheName + "' not found");
        response.put("status", "error");

        return ResponseEntity.notFound().build();
    }

    /**
     * Clear ALL caches at once
     *
     * Example: DELETE /api/cache/all
     * Nuclear option - use with caution!
     */
    @DeleteMapping("/all")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        log.info("Request to clear ALL caches");

        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cleared cache: {}", cacheName);
            }
        });

        Map<String, String> response = new HashMap<>();
        response.put("message", "All caches cleared successfully");
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

    /**
     * Get information about all available caches
     *
     * Example: GET /api/cache/info
     * Shows which caches exist in the application
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        log.info("Request for cache information");

        Map<String, Object> info = new HashMap<>();
        info.put("availableCaches", cacheManager.getCacheNames());
        info.put("cacheCount", cacheManager.getCacheNames().size());

        return ResponseEntity.ok(info);
    }
}