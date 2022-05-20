package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> findAttrInfoAndValueByCategoryId(Long c1Id, Long c2Id, Long c3Id);

    /**
     * 添加 平台属性
     * @param baseAttrInfo
     */

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 添加 或 修改 平台属性
     * @param baseAttrInfo
     */
    void saveOrUpdateAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 修改 平台属性
     * @param baseAttrInfo
     */
    void updateAttrInfo(BaseAttrInfo baseAttrInfo);
}
