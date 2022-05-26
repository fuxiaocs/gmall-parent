package com.atguigu.gmall.common.cache.redisson;

import org.redisson.api.RBloomFilter;

public interface BloomTask {

    void initData(RBloomFilter<Object> bloomFilter);
}
