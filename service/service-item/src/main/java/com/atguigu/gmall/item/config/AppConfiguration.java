package com.atguigu.gmall.item.config;

import com.atguigu.gmall.common.annotation.EnableAppThreadPoolAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//@EnableAutoCache
//@EnableAppRedisCache   //抽取出 gmall-cache-starter

@EnableAspectJAutoProxy
@EnableAppThreadPoolAutoConfiguration
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.feign.product",
        "com.atguigu.gmall.feign.list"})
@Configuration
public class AppConfiguration {
}
