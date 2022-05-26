package com.atguigu.gmall.common.cache.service.impl;

import com.atguigu.gmall.common.cache.service.RedisCacheService;
import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.common.util.JSONs;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public <T> T getCacheData(String categoryRedisKey, TypeReference<T> typeReference) {
        String cacheData = redisTemplate.opsForValue().get(categoryRedisKey);
        T t = null;
        if (!StringUtils.isEmpty(cacheData)){ //不为空
            if (CacheConstant.CACHE_NULL.equals(cacheData)){ //取出来的是否为"null"
                t = JSONs.nullInstance(typeReference);
            } else {
                t = JSONs.strToObj(cacheData,typeReference);
            }
            return t;
        }
        return t;
    }

    @Override
    public void save(String categoryRedisKey, Object cacheData) {
        if (ObjectUtils.isEmpty(cacheData)){ //数据为空
            redisTemplate.opsForValue().set(
                    categoryRedisKey,
                    CacheConstant.CACHE_NULL,
                    CacheConstant.NULL_CACHE_TIME,
                    CacheConstant.NULL_CACHE_UNIT);
        } else {
            String cacheStr = JSONs.toStr(cacheData);
            redisTemplate.opsForValue().set(
                    categoryRedisKey,
                    cacheStr,
                    CacheConstant.EXIST_CACHE_TIME,
                    CacheConstant.EXIST_CACHE_UNIT);
        }
    }
}
