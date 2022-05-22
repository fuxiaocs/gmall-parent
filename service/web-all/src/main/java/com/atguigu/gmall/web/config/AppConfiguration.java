package com.atguigu.gmall.web.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.atguigu.gmall.web.rpc")
@Configuration
public class AppConfiguration {
}
