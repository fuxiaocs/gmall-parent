package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);

    void uploadOrDownSale(Long skuId, int status);

    SkuInfo getSkuInfoBySkuId(Long skuId);

    BigDecimal getSkuPriceBySkuId(Long skuId);

    List<SpuSaleAttr> getSpuSaleAttrListBySkuId(Long skuId);

    List<Long> getAllSkuId();
}
