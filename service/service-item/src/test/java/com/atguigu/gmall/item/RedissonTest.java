package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void bloomTest(){
        RBloomFilter<Long> bloom = redissonClient.getBloomFilter("testBloom");

        if (!bloom.isExists()) {
            bloom.tryInit(500,0.001);
        }

        bloom.add(77L);
        bloom.add(88L);
        bloom.add(99L);

        System.out.println(bloom.contains(77L));
        System.out.println(bloom.contains(99L));
        System.out.println(bloom.contains(990L));
    }
}
