package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseAttrValueService extends IService<BaseAttrValue> {

    /**
     * 根据平台id 获取属性
     * @param attrId
     * @return
     */
    List<BaseAttrValue> findAttrValueListByAttrId(Long attrId);

    void updateAttrValue(BaseAttrInfo baseAttrInfo);
}
