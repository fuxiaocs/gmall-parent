package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.cron.SkuIdBloomTaskImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/product")
@RestController
public class BloomTaskController {

    @Autowired
    SkuIdBloomTaskImpl skuIdBloomTaskImpl;

    @RequestMapping("/rebuildskuidbloom")
    public Result rebuildSkuIdBloom(){
        skuIdBloomTaskImpl.rebuildBloom();
        return Result.ok();
    }
}
