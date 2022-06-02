package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.mapper.SkuAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue>
    implements SkuAttrValueService{

    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;

    /**
     * 根据skuId 获取平台属性名和值
     * @param skuId
     * @return
     */
    @Override
    public List<SearchAttr> getAttrNameAndValueBySkuId(Long skuId) {
        return skuAttrValueMapper.getAttrNameAndValueBySkuId(skuId);
    }
}




