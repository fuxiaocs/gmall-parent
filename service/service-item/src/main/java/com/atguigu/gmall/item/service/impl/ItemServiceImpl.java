package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ItemDetailedTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ProductFeignClient productFeignClient;
    /**
     * 根据 skuId 获取商品详细信息
     * @param skuId
     * @return
     */
    @Override
    public ItemDetailedTo getItemDetailed(Long skuId) {
        ItemDetailedTo itemDetailedTo = new ItemDetailedTo();
        //获取BaseCategoryView 信息
        Result<BaseCategoryView> resultBaseCategoryView = productFeignClient.getBaseCategoryViewBySkuId(skuId);
        if (resultBaseCategoryView.isOk()){
            BaseCategoryView baseCategoryView = resultBaseCategoryView.getData();
            itemDetailedTo.setCategoryView(baseCategoryView);
        }

        //获取SkuInfo 信息
        Result<SkuInfo> resultSkuInfo = productFeignClient.getSkuInfoBySkuId(skuId);
        if (resultSkuInfo.isOk()){
            SkuInfo skuInfo = resultSkuInfo.getData();
            itemDetailedTo.setSkuInfo(skuInfo);
        }

        //获取价格
        Result<BigDecimal> resultPrice = productFeignClient.getSkuPriceBySkuId(skuId);
        if (resultPrice.isOk()){
            BigDecimal price = resultPrice.getData();
            itemDetailedTo.setPrice(price);
        }

        // 获取 SpuSaleAttr集合
        Result<List<SpuSaleAttr>> resultSpuSaleAttrList = productFeignClient.getSpuSaleAttrListBySkuId(skuId);
        if (resultSpuSaleAttrList.isOk()){
            List<SpuSaleAttr> spuSaleAttrList = resultSpuSaleAttrList.getData();
            itemDetailedTo.setSpuSaleAttrList(spuSaleAttrList);
        }

        // 获取 valuesSkuJson
        Result<Map<String, String>> resultValuesSkuJsonMap = productFeignClient.getValuesSkuJsonBySkuId(skuId);
        if (resultValuesSkuJsonMap.isOk()){
            Map<String, String> valuesSkuJsonMap = resultValuesSkuJsonMap.getData();
            String valuesSkuJson = JSONs.toStr(valuesSkuJsonMap);
            itemDetailedTo.setValuesSkuJson(valuesSkuJson);
        }

        return itemDetailedTo;
    }
}
