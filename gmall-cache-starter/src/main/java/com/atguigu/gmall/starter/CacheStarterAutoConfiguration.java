package com.atguigu.gmall.starter;

import com.atguigu.gmall.starter.annotation.EnableAppRedisCache;
import com.atguigu.gmall.starter.annotation.EnableAutoCache;
import com.atguigu.gmall.starter.threadpool.AppThreadPoolConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableAppRedisCache
@EnableAutoCache
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
@Import(AppThreadPoolConfiguration.class)
public class CacheStarterAutoConfiguration {
}
