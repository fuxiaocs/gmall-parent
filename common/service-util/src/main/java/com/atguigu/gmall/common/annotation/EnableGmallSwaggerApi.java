package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.Swagger2Config;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import(Swagger2Config.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableGmallSwaggerApi {
}
