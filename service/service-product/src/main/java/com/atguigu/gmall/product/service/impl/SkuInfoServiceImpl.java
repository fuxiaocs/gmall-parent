package com.atguigu.gmall.product.service.impl;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.product.service.*;
import com.google.common.collect.Lists;
import java.util.Date;

import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.feign.list.GoodsFeignClient;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.starter.cache.aop.AspectHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    SkuAttrValueService skuAttrValueService;

    @Autowired
    RBloomFilter<Object> skuIdBloom;

    @Autowired
    AspectHelper aspectHelper;

    @Autowired
    GoodsFeignClient goodsFeignClient;

    @Autowired
    BaseTrademarkService baseTrademarkService;

    @Autowired
    CategoryService categoryService;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //往sku_info表添加数据
        skuInfoMapper.insert(skuInfo);

        Long skuId = skuInfo.getId();
        Long spuId = skuInfo.getSpuId();

        //向skuIdBloom中添加 新的skuId
        skuIdBloom.add(skuId);

        //往sku_image表添加数据
        //封装skuId
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuId);
            skuImageService.save(skuImage);
        }

        //往sku_sale_attr_value表添加数据
        //封装skuId 和 spuId
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValue.setSpuId(spuId);
            skuSaleAttrValueService.save(skuSaleAttrValue);
        }

        //往sku_attr_value表添加数据
        //封装skuId
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
            skuAttrValueService.save(skuAttrValue);
        }
    }

    /**
     * 商品 上下架
     * @param skuId
     * @param status
     */
    @Override
    public void uploadOrDownSale(Long skuId, int status) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(status);
        skuInfoMapper.updateById(skuInfo);

        // 缓存 使用 延迟双删
        aspectHelper.deleteCache(CacheConstant.SKU_CACHE_KEY_PREFIX + skuId);
        //给ES ,保存/删除数据
        if (status == 1){
            //商品上架
            Goods goods = this.getGoodsBySkuId(skuId);

            goodsFeignClient.saveGoods(goods);
        }

        if (status == 0){
            //下架商品
            goodsFeignClient.deleteGoods(skuId);
        }
    }

    /**
     * 根据 skuId 获取SkuInfo信息 包括图片集合
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getSkuInfoBySkuId(Long skuId) {
        return skuInfoMapper.selectSkuInfoBySkuId(skuId);
    }

    /**
     * 根据 skuId 查询sku价格
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal getSkuPriceBySkuId(Long skuId) {
        return skuInfoMapper.selectSkuPriceBySkuId(skuId);
    }

    /**
     * 根据 skuId 查询 spuSaleAttr集合信息
     * @param skuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListBySkuId(Long skuId) {
        return skuInfoMapper.selectSpuSaleAttrListBySkuId(skuId);
    }

    /**
     * 获取 所有的skuId  用于布隆
     * @return
     */
    @Override
    public List<Long> getAllSkuId() {
        return skuInfoMapper.selectAllSkuId();
    }

    /**
     * 根据skuId 获取goods
     * @return
     */
    @Override
    public Goods getGoodsBySkuId(Long skuId) {
        Goods goods = new Goods();

        //skuInfo 信息
        SkuInfo skuInfo = this.getSkuInfoBySkuId(skuId);
        goods.setId(skuInfo.getId());
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());

        //sku 品牌信息
        BaseTrademark trademark = baseTrademarkService.getById(skuInfo.getTmId());
        goods.setTmId(trademark.getId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());

        //获取商品 三级分类
        BaseCategoryView category = categoryService.getBaseCategoryViewBySkuId(skuId);
        goods.setCategory1Id(category.getCategory1Id());
        goods.setCategory1Name(category.getCategory1Name());
        goods.setCategory2Id(category.getCategory2Id());
        goods.setCategory2Name(category.getCategory2Name());
        goods.setCategory3Id(category.getCategory3Id());
        goods.setCategory3Name(category.getCategory3Name());

        //商品热度
        goods.setHotScore(0L);

        //平台属性
        List<SearchAttr> searchAttrs = skuAttrValueService.getAttrNameAndValueBySkuId(skuId);
        goods.setAttrs(searchAttrs);


        return goods;
    }
}




