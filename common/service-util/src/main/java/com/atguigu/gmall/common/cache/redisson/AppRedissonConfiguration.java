package com.atguigu.gmall.common.cache.redisson;

import com.atguigu.gmall.common.constants.CacheConstant;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class AppRedissonConfiguration {

    @Autowired(required = false)
    List<BloomTask> bloomTasks;

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword());
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    @Bean
    public BloomTask bloomTask(){
        return bloomFilter -> { };
    }

    @Bean
    public RBloomFilter<Object> skuIdBloom(RedissonClient redissonClient){
        RBloomFilter<Object> bloom = redissonClient.getBloomFilter(CacheConstant.BLOOM_SKU_ID);

        if (!bloom.isExists()){
            for (BloomTask bloomTask : bloomTasks) {
                if (bloomTask instanceof SkuIdBloomTask){
                    bloomTask.initData(bloom);
                }
            }
        }
        return bloom;
    }
}
