package com.example.vhandler.service;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

public interface CacheHandlerService<K, V> {

    RedisTemplate getRedisTemplate();
    String getCacheName();

    default void save(K key ,V object) {
        RedisTemplate redisTemplate = getRedisTemplate();
        String cacheName = getCacheName();

        redisTemplate.opsForHash().put(cacheName, key, object);
    }

    default void saveAll(Map<K, V> objects) {
        RedisTemplate redisTemplate = getRedisTemplate();
        String cacheName = getCacheName();

        redisTemplate.opsForHash().putAll(cacheName, objects);
    }

    default V findById(K key) {
        RedisTemplate redisTemplate = getRedisTemplate();
        String cacheName = getCacheName();

        return (V) redisTemplate.opsForHash().get(cacheName, key);
    }

    default Map<K, V> findAll() {
        RedisTemplate redisTemplate = getRedisTemplate();
        String cacheName = getCacheName();

        return redisTemplate.opsForHash().entries(cacheName);
    }

    default void deleteById(K... key) {
        RedisTemplate redisTemplate = getRedisTemplate();
        String cacheName = getCacheName();

        redisTemplate.opsForHash().delete(cacheName, key);
    }
}
