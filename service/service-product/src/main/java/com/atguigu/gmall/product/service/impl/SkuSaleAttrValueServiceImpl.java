package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.to.ValueJsonTo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.atguigu.gmall.product.mapper.SkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue>
    implements SkuSaleAttrValueService{

    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    /**
     * 根据 skuId 获取valuesSkuJson  "119|120":"49"
     * @param skuId
     * @return
     */
    @Override
    public List<ValueJsonTo> getValuesSkuJsonBySkuId(Long skuId) {
        return skuSaleAttrValueMapper.selectValuesSkuJsonBySkuId(skuId);
    }
}




