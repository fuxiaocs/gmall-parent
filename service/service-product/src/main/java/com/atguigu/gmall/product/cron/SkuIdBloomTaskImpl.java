package com.atguigu.gmall.product.cron;

import com.atguigu.gmall.common.cache.redisson.SkuIdBloomTask;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SkuIdBloomTaskImpl implements SkuIdBloomTask {

//    @Autowired
//    SkuInfoService skuInfoService;

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Qualifier("skuIdBloom")
    @Autowired
    RBloomFilter<Object> skuIdBloom;

    /**
     * 定时重置布隆
     */
    @Scheduled(cron = "0 0 3 * * 3")
    public void rebuildBloom(){
        log.info("正在重置skuIdBloom : {}",skuIdBloom);
        skuIdBloom.delete();
        this.initData(skuIdBloom);
    }

    /**
     * 初始化布隆  和  添加数据
     * @param bloomFilter
     */
    @Override
    public void initData(RBloomFilter<Object> bloomFilter) {
        log.info("正在初始化添加数据skuIdBloom");
        bloomFilter.tryInit(5000000,0.0000001);
        //获取所有 skuId
        List<Long> skuIds = skuInfoMapper.selectAllSkuId();
        for (Long skuId : skuIds) {
            bloomFilter.add(skuId);
        }
    }
}
