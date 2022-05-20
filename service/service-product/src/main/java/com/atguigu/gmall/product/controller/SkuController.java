package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/product")
@RestController
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;

    /**
     * 分页查询 SkuInfo
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/list/{page}/{limit}")
    public Result getSkuInfoListByPage(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit){
        Page<SkuInfo> resultPage = new Page<>(page,limit);
        skuInfoService.page(resultPage);
        return Result.ok(resultPage);
    }

    /**
     * 添加 SkuInfo信息
     * @param skuInfo
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    /**
     * 商品 上架
     * @param skuId
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result uploadSale(@PathVariable("skuId") Long skuId){
        skuInfoService.uploadOrDownSale(skuId,1);
        return Result.ok();
    }

    /**
     * 商品 下架
     * @param skuId
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result downSale(@PathVariable("skuId") Long skuId){
        skuInfoService.uploadOrDownSale(skuId,0);
        return Result.ok();
    }
}
