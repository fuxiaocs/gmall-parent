package com.atguigu.gmall.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.*;

@ConfigurationProperties(prefix = "app.threadpool")
@Data
public class AppThreadPoolProperties {
    //int corePoolSize,
    //int maximumPoolSize,
    //long keepAliveTime,
    //TimeUnit unit,
    //BlockingQueue<Runnable> workQueue,
    //ThreadFactory threadFactory,
    //RejectedExecutionHandler handler
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
    private TimeUnit timeUnit= TimeUnit.MINUTES;
    private Integer queueSize;
//    private BlockingQueue workQueue = new LinkedBlockingQueue(queueSize);
    private RejectedExecutionHandler handler =
            new ThreadPoolExecutor.CallerRunsPolicy();
}
