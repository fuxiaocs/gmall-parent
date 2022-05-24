package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
public class ThreadPoolTest {

//    @Qualifier("otherThreadPool")
    @Autowired
    ThreadPoolExecutor threadPool;

    @Test
    public void appThreadPoolTest(){
        System.out.println(threadPool.getCorePoolSize());
    }

    @Test
    public void completableFutureTest() throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<Void> run1 = CompletableFuture.runAsync(() -> {
            System.out.println("001开始执行");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("001执行完成");
        }, threadPool);

        CompletableFuture<Void> run2 = CompletableFuture.runAsync(() -> {
            System.out.println("002开始执行");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("002执行完成");
        }, threadPool);

        CompletableFuture<Void> run3 = CompletableFuture.runAsync(() -> {
            System.out.println("003开始执行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("003执行完成");
        }, threadPool);

        CompletableFuture.allOf(run1,run2,run3).get(4000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void allAndAnyTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> supply1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("001开始执行");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("001执行完成");
            return 1;
        }, threadPool);

        CompletableFuture<Integer> supply2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("002开始执行");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("002执行完成");
            return 2;
        }, threadPool);

        CompletableFuture<Integer> supply3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("003开始执行");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("003执行完成");
            return 3;
        }, threadPool);

        Object o = CompletableFuture.anyOf(supply1, supply2, supply3).get();
        System.out.println(o);
        Void aVoid = CompletableFuture.allOf(supply1, supply2, supply3).get();
        System.out.println(aVoid);
        Integer integer = supply2.get();
        System.out.println(integer);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(supply1);
        System.out.println(voidCompletableFuture);
    }
}
