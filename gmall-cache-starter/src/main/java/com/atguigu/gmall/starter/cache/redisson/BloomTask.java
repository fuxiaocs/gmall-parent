package com.atguigu.gmall.starter.cache.redisson;

import org.redisson.api.RBloomFilter;

public interface BloomTask {

    void initData(RBloomFilter<Object> bloomFilter);
}
