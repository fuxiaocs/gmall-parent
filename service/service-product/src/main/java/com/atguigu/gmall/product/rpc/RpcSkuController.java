package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.CategoryService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.atguigu.gmall.product.to.ValueJsonTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rpc/inner/product")
public class RpcSkuController {

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * 根据 skuId 获取SkuInfo信息 包括图片集合
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuInfoBySkuId/{skuId}")
    public Result getSkuInfoBySkuId(@PathVariable("skuId") Long skuId){
        SkuInfo skuInfo = skuInfoService.getSkuInfoBySkuId(skuId);
        return Result.ok(skuInfo);
    }

    /**
     * 根据 skuId 查询sku价格
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuPriceBySkuId/{skuId}")
    public Result getSkuPriceBySkuId(@PathVariable("skuId") Long skuId){
        BigDecimal price = skuInfoService.getSkuPriceBySkuId(skuId);
        return Result.ok(price);
    }

    /**
     * 根据 skuId 查询 spuSaleAttr集合信息
     * @param skuId
     * @return
     */
    @GetMapping("/getSpuSaleAttrListBySkuId/{skuId}")
    public Result getSpuSaleAttrListBySkuId(@PathVariable("skuId") Long skuId){
        List<SpuSaleAttr> list = skuInfoService.getSpuSaleAttrListBySkuId(skuId);
        return Result.ok(list);
    }

    /**
     * 根据 skuId 获取valuesSkuJson  "119|120":"49"
     * @param skuId
     * @return
     */
    @GetMapping("/getValuesSkuJsonBySkuId/{skuId}")
    public Result getValuesSkuJsonBySkuId(@PathVariable("skuId") Long skuId){
        List<ValueJsonTo> list = skuSaleAttrValueService.getValuesSkuJsonBySkuId(skuId);
        Map<String, String> resultMap = new HashMap<>();
        for (ValueJsonTo valueJsonTo : list) {
            String id = valueJsonTo.getId();
            String valueJson = valueJsonTo.getValueJson();
            resultMap.put(valueJson,id);
        }
        return Result.ok(resultMap);
    }

}
