package com.atguigu.gmall.product.annocation;

import com.atguigu.gmall.common.annotation.*;
import com.atguigu.gmall.common.config.AppMyBatisPlusConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@EnableAutoCache
//@EnableAppRedisCache  //抽取出 gmall-cache-starter

@EnableScheduling
@EnableGmallGlobbalExceptionHandler
@EnableMinIO
@EnableGmallSwaggerApi
@Import(AppMyBatisPlusConfiguration.class)
@Configuration
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductConfiguration {
}
