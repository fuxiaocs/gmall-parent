package com.atguigu.gmall.item.config;

import com.atguigu.gmall.common.annotation.EnableAppRedisCache;
import com.atguigu.gmall.common.annotation.EnableAppThreadPoolAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableAppRedisCache
@EnableAppThreadPoolAutoConfiguration
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.product")
@Configuration
public class AppConfiguration {
}
