package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.cache.service.RedisCacheService;
import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ItemDetailedTo;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 根据 skuId 查询数据 先看缓存 和 布隆过滤器
     * @param skuId
     * @return
     */
    @Override
    public ItemDetailedTo getItemDetailed(Long skuId) {
        //在缓存中的key
        String skuCacheKey = CacheConstant.SKU_CACHE_KEY_PREFIX + skuId;
        //查询缓存 是否有
        ItemDetailedTo cacheData = redisCacheService.getCacheData(
                skuCacheKey,
                new TypeReference<ItemDetailedTo>() {
                });

        if (ObjectUtils.isEmpty(cacheData)){ //查询缓存为空
            log.info("{} : 缓存未命中,准备访问bloom",skuCacheKey);
            //查询布隆 数据库是否存在 该skuId
            if (skuIdBloom.contains(skuId)){ //布隆 觉得skuId存在
                log.info("{} : bloom觉得这个skuId存在",skuId);
                //去数据库查询
                cacheData = this.getItemDetailedDb(skuId);
                //存入缓存
                redisCacheService.save(skuCacheKey,cacheData);
            } else { //布隆 说不存在
                log.info("{} : bloom觉得这个skuId不存在,直接返回null",skuId);
                return null;
            }
        }

        //缓存中有 返回
        log.info("{} : 缓存命中,直接返回",skuCacheKey);
        return cacheData;
    }

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

        return itemDetailedTo;
    }
}
