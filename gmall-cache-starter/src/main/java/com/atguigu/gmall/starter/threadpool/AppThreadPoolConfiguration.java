package com.atguigu.gmall.starter.threadpool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AppThreadPoolConfiguration {

    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPool(
            @Value("${spring.application.name}") String appName){
        return new ScheduledThreadPoolExecutor(
                5,
                new AppThreadFactory(appName),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}

class AppThreadFactory implements ThreadFactory {
    private String appName;

    private AtomicInteger count = new AtomicInteger(1);
    public AppThreadFactory(String appName){
        this.appName = appName;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(
                r,
                appName + "-" + count.getAndIncrement()
        );
    }
}
