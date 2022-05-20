package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //往sku_info表添加数据
        skuInfoMapper.insert(skuInfo);

        Long skuId = skuInfo.getId();
        Long spuId = skuInfo.getSpuId();

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
}




