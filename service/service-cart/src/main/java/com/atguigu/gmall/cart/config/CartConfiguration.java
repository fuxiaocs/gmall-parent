package com.atguigu.gmall.cart.config;

import com.atguigu.gmall.common.annotation.EnableAppThreadPoolAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableAppThreadPoolAutoConfiguration
@EnableFeignClients("com.atguigu.gmall.feign.product")
@Configuration
public class CartConfiguration {
}
