package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.to.ItemDetailedTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpc/inner/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    /**
     * 根据 skuId 获取商品详细信息
     * @param skuId
     * @return
     */
    @GetMapping("/getItemDetailed/{skuId}")
    public Result getItemDetailed(@PathVariable("skuId") Long skuId){

        ItemDetailedTo itemDetailedTo = itemService.getItemDetailed(skuId);
        return Result.ok(itemDetailedTo);
    }
}
