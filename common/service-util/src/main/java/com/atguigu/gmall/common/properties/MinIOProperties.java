package com.atguigu.gmall.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.minio")
@Data
public class MinIOProperties {

    String endpoint;
    String accessKey;
    String secretKey;
    String bucket;
}
