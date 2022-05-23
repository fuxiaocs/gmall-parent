package com.atguigu.gmall.feign.product;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryAndChild;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient("service-product")
@RequestMapping("rpc/inner/product")
public interface ProductFeignClient {

    /**
     * 获取所有分类与子分类
     * @return
     */
    @GetMapping("/getAllCategoryAndChild")
    Result<List<CategoryAndChild>> getAllCategoryAndChild();

    /**
     * 根据 skuId 获取分类层级
     * @param skuId
     * @return
     */
    @GetMapping("/getBaseCategoryViewBySkuId/{skuId}")
    Result<BaseCategoryView> getBaseCategoryViewBySkuId(
            @PathVariable("skuId") Long skuId);

    /**
     * 根据 skuId 获取SkuInfo信息 包括图片集合
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuInfoBySkuId/{skuId}")
    Result<SkuInfo> getSkuInfoBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 根据 skuId 查询sku价格
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuPriceBySkuId/{skuId}")
    Result<BigDecimal> getSkuPriceBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 根据 skuId 查询 spuSaleAttr集合信息
     * @param skuId
     * @return
     */
    @GetMapping("/getSpuSaleAttrListBySkuId/{skuId}")
    Result<List<SpuSaleAttr>> getSpuSaleAttrListBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 根据 skuId 获取valuesSkuJson  "119|120":"49"
     * @param skuId
     * @return
     */
    @GetMapping("/getValuesSkuJsonBySkuId/{skuId}")
    Result<Map<String,String>> getValuesSkuJsonBySkuId(@PathVariable("skuId") Long skuId);
}
