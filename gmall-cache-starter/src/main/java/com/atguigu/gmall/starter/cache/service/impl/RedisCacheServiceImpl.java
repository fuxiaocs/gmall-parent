package com.atguigu.gmall.starter.cache.service.impl;

import com.atguigu.gmall.starter.cache.service.RedisCacheService;
import com.atguigu.gmall.starter.constants.CacheConstant;
import com.atguigu.gmall.starter.util.JSONs;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
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
        //这里有些情况 不能用 == null  或 ObjectUtils.isEmpty()
        //自己new的对象 不会为空
        if (ObjectUtils.isEmpty(cacheData)){ //数据为空
            log.info("数据为空 : {}",cacheData);
            redisTemplate.opsForValue().set(
                    categoryRedisKey,
                    CacheConstant.CACHE_NULL,
                    CacheConstant.NULL_CACHE_TIME,
                    CacheConstant.NULL_CACHE_UNIT);
        } else {
            //随机生成 时间
            log.info("数据存在 : {}",cacheData);
            Double time = Math.random() * 1000000000;
            String cacheStr = JSONs.toStr(cacheData);
            redisTemplate.opsForValue().set(
                    categoryRedisKey,
                    cacheStr,
                    //解决 缓存雪崩 避免大量数据同一时刻过期
                    CacheConstant.EXIST_CACHE_TIME + time.intValue(),
                    CacheConstant.EXIST_CACHE_UNIT);
        }
    }
}
