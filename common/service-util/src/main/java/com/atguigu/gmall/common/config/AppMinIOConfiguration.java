package com.atguigu.gmall.common.config;

import com.atguigu.gmall.common.minio.OSSService;
import com.atguigu.gmall.common.minio.impl.OSSServiceImpl;
import com.atguigu.gmall.common.properties.MinIOProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(MinIOProperties.class)
@Configuration
public class AppMinIOConfiguration {

    @Autowired
    MinIOProperties minIOProperties;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient = new MinioClient(
                minIOProperties.getEndpoint(),
                minIOProperties.getAccessKey(),
                minIOProperties.getSecretKey()
        );
        //桶是否存在
        boolean exists = minioClient.bucketExists(minIOProperties.getBucket());
        if (!exists) { //不存在创建桶
            minioClient.makeBucket(minIOProperties.getBucket());
        }
        return minioClient;
    }

    @Bean
    public OSSService ossService(){
        return new OSSServiceImpl();
    }
}
