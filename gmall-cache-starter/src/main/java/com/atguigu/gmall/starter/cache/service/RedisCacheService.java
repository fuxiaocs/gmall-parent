package com.atguigu.gmall.starter.cache.service;

import com.fasterxml.jackson.core.type.TypeReference;


public interface RedisCacheService {
    <T> T getCacheData(String categoryRedisKey, TypeReference<T> typeReference);

    void save(String categoryRedisKey, Object cacheData);
}
