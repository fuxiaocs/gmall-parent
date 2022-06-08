package com.atguigu.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("app.auth")
public class AuthProperties {

    private List<String> anyoneurls;

    private List<String> unanyoneurls;

    private List<String> authurls;

    private String loginpage;
}
