package com.tech.agendaai.company.config;

import org.springframework.boot.cache.autoconfigure.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

public class CacheManagerConfig {

    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return cacheManager -> {cacheManager.setAllowNullValues(false);
        cacheManager.};
    }
}
