package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    List<SearchAttr> getAttrNameAndValueBySkuId(Long skuId);
}
