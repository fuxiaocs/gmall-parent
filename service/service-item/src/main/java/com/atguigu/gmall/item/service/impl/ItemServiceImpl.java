package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.starter.cache.service.RedisCacheService;
import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ItemDetailedTo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    ThreadPoolExecutor threadPool;

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    RBloomFilter<Object> skuIdBloom;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    /**
     * 根据 skuId 获取 商品详情信息  (先看缓存 和 布隆过滤器  +  redisson锁) 整合切面
     * @param skuId
     * @return
     */
    @Cache(
            cacheKey = CacheConstant.SKU_CACHE_KEY_PREFIX + "#{#args[0]}",
            bloomFilterName = "skuIdBloom",
            bloomValue = "#{#args[0]}")
    @Override
    public ItemDetailedTo getItemDetailed(Long skuId) {
        return this.getItemDetailedDb(skuId);
    }

//    /**
//     * 根据 skuId 获取 商品详情信息  先看缓存 和 布隆过滤器  +  redisson锁
//     * @param skuId
//     * @return
//     */
//    @Override
//    public ItemDetailedTo getItemDetailed(Long skuId) {
//        //数据在缓存中的键
//        String skuCacheKey = CacheConstant.SKU_CACHE_KEY_PREFIX + skuId;
//        //查询的数据类型
//        TypeReference<ItemDetailedTo> typeReference = new TypeReference<ItemDetailedTo>() {};
//        //先 查询缓存 看缓存是否存在数据
//        log.info("{} : 准备缓存查询数据" , skuCacheKey);
//        ItemDetailedTo itemDetailedTo = redisCacheService.getCacheData(
//                skuCacheKey,
//                typeReference);
//        if (ObjectUtils.isEmpty(itemDetailedTo)){ //数据为空  缓存中不存在
//            log.info("{} : 缓存未命中" , skuCacheKey);
//            //询问 bloom 数据库中是否存在 该skuId
//            if (skuIdBloom.contains(skuId)) { // bloom 觉得存在 准备访问数据库
//                log.info("{} : Bloom觉得有,准备抢锁 查询数据库",skuId);
//                //添加 锁 只抢一次  抢到访问数据库  没抢到等1s 访问缓存 返回
//                //锁名
//                String lockName = CacheConstant.LOCK_SKU_PREFIX + skuId;
//                RLock lock = redissonClient.getLock(lockName);
//                //是否抢到锁
//                if (lock.tryLock()) { //抢到锁
//                    try {
//                        log.info("{} : 抢到锁,准备查询数据库" , skuId);
//                        //操作数据库
//                        itemDetailedTo = this.getItemDetailedDb(skuId);
//                        //将查询到的数据 存入 缓存
//                        redisCacheService.save(skuCacheKey,itemDetailedTo);
//                    } finally { // 释放锁
//                        try {
//                            lock.unlock();
//                        } catch (Exception e) {
//                            log.error("释放锁异常 : {}",e);
//                        }
//                    }
//                } else { //没有强到锁
//                    log.info("没有抢到锁,等待1s,在次查缓存");
//                    //睡 1s
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        log.error("睡眠异常 : {}" ,e);
//                    }
//                    //再查询一次缓存
//                    itemDetailedTo = redisCacheService.getCacheData(skuCacheKey,typeReference);
//                }
//                return itemDetailedTo;
//            } else { //bloom 觉得 不存在  一定不存在
//                log.info("{} : Bloom说不存在 , 直接返回null",skuId);
//                return null;
//            }
//        }
//
//        //缓存存在 返回数据
//        log.info("{} : 缓存中查询到数据,返回数据" , skuCacheKey);
//        return itemDetailedTo;
//    }

//    /**
//     * (没有加分布式锁)
//     * 根据 skuId 查询数据 先看缓存 和 布隆过滤器
//     * @param skuId
//     * @return
//     */
//    @Override
//    public ItemDetailedTo getItemDetailed(Long skuId) {
////        stringRedisTemplate.opsForValue().setIfAbsent();
////        stringRedisTemplate.delete();
//        //在缓存中的key
//        String skuCacheKey = CacheConstant.SKU_CACHE_KEY_PREFIX + skuId;
//        //查询缓存 是否有
//        ItemDetailedTo cacheData = redisCacheService.getCacheData(
//                skuCacheKey,
//                new TypeReference<ItemDetailedTo>() {
//                });
//
//        if (ObjectUtils.isEmpty(cacheData)){ //查询缓存为空
//            log.info("{} : 缓存未命中,准备访问bloom",skuCacheKey);
//            //查询布隆 数据库是否存在 该skuId
//            if (skuIdBloom.contains(skuId)){ //布隆 觉得skuId存在
//                log.info("{} : bloom觉得这个skuId存在",skuId);
//                //去数据库查询
//                cacheData = this.getItemDetailedDb(skuId);
//                //存入缓存
//                redisCacheService.save(skuCacheKey,cacheData);
//            } else { //布隆 说不存在
//                log.info("{} : bloom觉得这个skuId不存在,直接返回null",skuId);
//                return null;
//            }
//        }
//
//        //缓存中有 返回
//        log.info("{} : 缓存命中,直接返回",skuCacheKey);
//        return cacheData;
//    }

    /**
     * 根据 skuId 去数据库 获取商品详细信息
     * @param skuId
     * @return
     */
    public ItemDetailedTo getItemDetailedDb(Long skuId) {
        ItemDetailedTo itemDetailedTo = new ItemDetailedTo();
        //获取BaseCategoryView 信息
        CompletableFuture<Void> categoryTask = CompletableFuture.runAsync(() -> {
            Result<BaseCategoryView> resultBaseCategoryView = productFeignClient.getBaseCategoryViewBySkuId(skuId);
            if (resultBaseCategoryView.isOk()) {
                BaseCategoryView baseCategoryView = resultBaseCategoryView.getData();
                itemDetailedTo.setCategoryView(baseCategoryView);
            }
        }, threadPool);


        //获取SkuInfo 信息
        CompletableFuture<Void> skuInfoTask = CompletableFuture.runAsync(() -> {
            Result<SkuInfo> resultSkuInfo = productFeignClient.getSkuInfoBySkuId(skuId);
            if (resultSkuInfo.isOk()) {
                SkuInfo skuInfo = resultSkuInfo.getData();
                itemDetailedTo.setSkuInfo(skuInfo);
            }
        }, threadPool);


        //获取价格
        CompletableFuture<Void> priceTask = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> resultPrice = productFeignClient.getSkuPriceBySkuId(skuId);
            if (resultPrice.isOk()) {
                BigDecimal price = resultPrice.getData();
                itemDetailedTo.setPrice(price);
            }
        }, threadPool);


        // 获取 SpuSaleAttr集合
        CompletableFuture<Void> spuSaleAttrTask = CompletableFuture.runAsync(() -> {
            Result<List<SpuSaleAttr>> resultSpuSaleAttrList = productFeignClient.getSpuSaleAttrListBySkuId(skuId);
            if (resultSpuSaleAttrList.isOk()) {
                List<SpuSaleAttr> spuSaleAttrList = resultSpuSaleAttrList.getData();
                itemDetailedTo.setSpuSaleAttrList(spuSaleAttrList);
            }
        }, threadPool);


        // 获取 valuesSkuJson
        CompletableFuture<Void> valuesSkuJsonTask = CompletableFuture.runAsync(() -> {
            Result<Map<String, String>> resultValuesSkuJsonMap = productFeignClient.getValuesSkuJsonBySkuId(skuId);
            if (resultValuesSkuJsonMap.isOk()) {
                Map<String, String> valuesSkuJsonMap = resultValuesSkuJsonMap.getData();
                String valuesSkuJson = JSONs.toStr(valuesSkuJsonMap);
                itemDetailedTo.setValuesSkuJson(valuesSkuJson);
            }
        }, threadPool);

        CompletableFuture.allOf(
                categoryTask,
                skuInfoTask,
                priceTask,
                spuSaleAttrTask,
                valuesSkuJsonTask).join();

        if (ObjectUtils.isEmpty(itemDetailedTo.getSkuInfo())) {
            return null;
        }

        return itemDetailedTo;
    }
}
