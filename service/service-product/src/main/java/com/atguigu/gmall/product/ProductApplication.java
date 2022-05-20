package com.atguigu.gmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

//@EnableTransactionManagement
//@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
//@Import(AppMyBatisPlusConfiguration.class)
@SpringCloudApplication
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
