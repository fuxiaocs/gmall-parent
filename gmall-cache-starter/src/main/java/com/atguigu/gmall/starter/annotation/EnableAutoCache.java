package com.atguigu.gmall.starter.annotation;

import com.atguigu.gmall.starter.cache.aop.AspectHelper;
import com.atguigu.gmall.starter.cache.aop.CacheAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Import({CacheAspect.class,AspectHelper.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableAutoCache {
}
