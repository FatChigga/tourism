/*
package com.iptv.core.shiro.dao;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class ShiroRedisCache<K,V> implements Cache<K,V>{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String REDIS_SHIRO_CACHE = "slife-shiro-cache:";

    @Autowired
    private RedisTemplate<K, V> redisTemplate;

    @Value("${redis.expiration}")
    private long expireTime ;

    @Override
    public V get(K k) throws CacheException {
        redisTemplate.boundValueOps(getCacheKey(k)).expire(expireTime, TimeUnit.SECONDS);
        return redisTemplate.boundValueOps(getCacheKey(k)).get();
    }

    @Override
    public V put(K k, V v) throws CacheException {
        redisTemplate.boundValueOps(getCacheKey(k)).set(v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        V old = get(k);
        redisTemplate.delete(getCacheKey(k));
        return old;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    private K getCacheKey(Object k) {
        return (K) (REDIS_SHIRO_CACHE + k);
    }
}*/
