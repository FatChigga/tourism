/*
package com.iptv.core.shiro.dao;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShiroRedisCacheManager implements CacheManager {

    private  Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShiroRedisCache shiroRedisCache;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return shiroRedisCache;
    }
}*/
