package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Entity com.atguigu.gmall.product.domain.SkuInfo
 */
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    SkuInfo selectSkuInfoBySkuId(@Param("skuId") Long skuId);

    BigDecimal selectSkuPriceBySkuId(@Param("skuId") Long skuId);

    List<SpuSaleAttr> selectSpuSaleAttrListBySkuId(@Param("skuId") Long skuId);
}




