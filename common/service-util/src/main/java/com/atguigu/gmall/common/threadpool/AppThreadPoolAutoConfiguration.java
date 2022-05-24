package com.atguigu.gmall.common.threadpool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@EnableConfigurationProperties(AppThreadPoolProperties.class)
@Configuration
public class AppThreadPoolAutoConfiguration {

    @Primary
    @Bean
    public ThreadPoolExecutor coreThreadPool(
            AppThreadPoolProperties threadProperties,
            @Value("spring.application.name") String appName){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                threadProperties.getCorePoolSize(),
                threadProperties.getMaximumPoolSize(),
                threadProperties.getKeepAliveTime(),
                threadProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(threadProperties.getQueueSize()/2),
                new AppThreadFactory(appName + "-core"),
                threadProperties.getHandler()
        );

        return threadPoolExecutor;
    }

    @Bean
    public ThreadPoolExecutor otherThreadPool(
            AppThreadPoolProperties threadProperties,
            @Value("spring.application.name") String appName){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                threadProperties.getCorePoolSize()/2,
                threadProperties.getMaximumPoolSize()/2,
                threadProperties.getKeepAliveTime(),
                threadProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(threadProperties.getQueueSize()/2),
                new AppThreadFactory(appName + "-core"),
                threadProperties.getHandler()
        );

        return threadPoolExecutor;
    }
}


class AppThreadFactory implements ThreadFactory{

    private String appName;
    private AtomicInteger count = new AtomicInteger(1);

    public AppThreadFactory(String appName){
        this.appName = appName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(
                r,
                appName + "-" + count.getAndIncrement());
        return thread;
    }
}
