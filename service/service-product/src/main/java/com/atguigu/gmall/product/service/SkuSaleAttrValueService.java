package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.to.ValueJsonTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    List<ValueJsonTo> getValuesSkuJsonBySkuId(Long skuId);
}
