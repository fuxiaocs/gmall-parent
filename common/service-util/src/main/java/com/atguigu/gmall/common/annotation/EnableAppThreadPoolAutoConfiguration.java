package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.threadpool.AppThreadPoolAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import(AppThreadPoolAutoConfiguration.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableAppThreadPoolAutoConfiguration {
}
