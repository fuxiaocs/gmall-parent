package com.atguigu.gmall.web.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.item.ItemFeignClient;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ItemDetailedTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Autowired
    ItemFeignClient itemFeignClient;

    @GetMapping("/{skuId}.html")
    public String getItemPage(@PathVariable("skuId") Long skuId, Model model){
        Result<ItemDetailedTo> resultItemDetailed = itemFeignClient.getItemDetailed(skuId);
        if (resultItemDetailed.isOk()){
            ItemDetailedTo itemDetailedTo = resultItemDetailed.getData();
            //categoryView
            BaseCategoryView categoryView = itemDetailedTo.getCategoryView();
            model.addAttribute("categoryView",categoryView);

            //skuInfo
            SkuInfo skuInfo = itemDetailedTo.getSkuInfo();
            model.addAttribute("skuInfo",skuInfo);

            //price
            BigDecimal price = itemDetailedTo.getPrice();
            model.addAttribute("price",price);

            //spuSaleAttrList
            List<SpuSaleAttr> spuSaleAttrList = itemDetailedTo.getSpuSaleAttrList();
            model.addAttribute("spuSaleAttrList",spuSaleAttrList);

            //valuesSkuJson
            String valuesSkuJson = itemDetailedTo.getValuesSkuJson();
            model.addAttribute("valuesSkuJson",valuesSkuJson);
        }

        return "item/index";
    }
}
