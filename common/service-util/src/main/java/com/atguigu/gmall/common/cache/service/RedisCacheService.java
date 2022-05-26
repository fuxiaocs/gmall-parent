package com.atguigu.gmall.common.cache.service;

import com.fasterxml.jackson.core.type.TypeReference;


public interface RedisCacheService {
    <T> T getCacheData(String categoryRedisKey, TypeReference<T> typeReference);

    void save(String categoryRedisKey, Object cacheData);
}
