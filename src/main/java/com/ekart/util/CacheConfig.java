package com.ekart.util;
//

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(List.of("byteArrayCache", "DownloadDocument", "Cache3"));

        return cacheManager;
    }

    @Bean
    public Serializer<ByteArrayInputStream> byteArrayInputStreamSerializer() {
        return new ByteArrayInputStreamSerializer();
    }

    @Bean
    public Deserializer<ByteArrayInputStream> byteArrayInputStreamDeserializer() {
        return new ByteArrayInputStreamSerializer();
    }
}
