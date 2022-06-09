package com.atguigu.gmall.cart.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients("com.atguigu.gmall.feign.product")
@Configuration
public class CartConfiguration {
}
