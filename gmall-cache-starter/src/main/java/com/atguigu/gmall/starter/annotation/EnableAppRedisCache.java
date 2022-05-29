package com.atguigu.gmall.starter.annotation;

import com.atguigu.gmall.starter.cache.redisson.AppRedissonConfiguration;
import com.atguigu.gmall.starter.cache.service.impl.RedisCacheServiceImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *  开启redis缓存
 *  redisson 和 布隆过滤器
 */
//@ComponentScan(basePackageClasses = RedisCacheServiceImpl.class)
@Import({RedisCacheServiceImpl.class, AppRedissonConfiguration.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableAppRedisCache {
}
