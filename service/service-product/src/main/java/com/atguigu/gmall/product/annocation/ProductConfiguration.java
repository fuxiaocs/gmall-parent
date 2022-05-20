package com.atguigu.gmall.product.annocation;

import com.atguigu.gmall.common.annotation.EnableGmallGlobbalExceptionHandler;
import com.atguigu.gmall.common.annotation.EnableGmallSwaggerApi;
import com.atguigu.gmall.common.annotation.EnableMinIO;
import com.atguigu.gmall.common.config.AppMyBatisPlusConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@EnableGmallGlobbalExceptionHandler
@EnableMinIO
@EnableGmallSwaggerApi
@Import(AppMyBatisPlusConfiguration.class)
@Configuration
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductConfiguration {
}
