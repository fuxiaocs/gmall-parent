package com.atguigu.gmall.list;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
@SpringCloudApplication
public class ListApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListApplication.class,args);
    }
}
