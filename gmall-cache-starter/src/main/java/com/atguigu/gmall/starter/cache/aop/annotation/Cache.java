package com.atguigu.gmall.starter.cache.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Cache {
    String value() default "";

    @AliasFor("value")
    String cacheKey() default "";

    String bloomFilterName() default "";

    String bloomValue() default "";



}
