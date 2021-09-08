package org.qamock.app.main.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("resource"),
                new ConcurrentMapCache("response"),
                new ConcurrentMapCache("jmstemplatemq"),
                new ConcurrentMapCache("jmstemplateamqp"),
                new ConcurrentMapCache("connfactorymq"),
                new ConcurrentMapCache("connfactoryamqp")
        ));
        return cacheManager;
    }
}
