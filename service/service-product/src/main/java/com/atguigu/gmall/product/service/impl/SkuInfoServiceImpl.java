package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SkuInfoService;
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

        //todo 给ES ,保存/删除数据
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
}




