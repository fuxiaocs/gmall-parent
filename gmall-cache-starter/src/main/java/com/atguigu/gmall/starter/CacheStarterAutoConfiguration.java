package com.atguigu.gmall.starter;

import com.atguigu.gmall.starter.annotation.EnableAppRedisCache;
import com.atguigu.gmall.starter.annotation.EnableAutoCache;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@EnableAppRedisCache
@EnableAutoCache
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class CacheStarterAutoConfiguration {
}
