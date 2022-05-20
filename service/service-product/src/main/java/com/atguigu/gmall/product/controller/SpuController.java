package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/product")
@RestController
public class SpuController {

    @Autowired
    SpuInfoService spuInfoService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuImageService spuImageService;

    /**
     * 分页查询 SpuInfo 信息
     * @param page
     * @param limit
     * @param c3Id
     * @return
     */
    @GetMapping("/{page}/{limit}")
    public Result getSpuInfoByPage(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            @RequestParam("category3Id") Long c3Id){
        Page<SpuInfo> resultPage = new Page<>(page,limit);
        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id",c3Id);
        spuInfoService.page(resultPage,queryWrapper);
        return Result.ok(resultPage);
    }

    /**
     * 添加 SpuInfo 信息
     * @param spuInfo
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    /**
     * 根据 spuId  查询SpuSaleAttr信息
     * @param spuId
     * @return
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable("spuId") Long spuId){
        List<SpuSaleAttr> list = spuSaleAttrService.getSpuSaleAttrList(spuId);
        return Result.ok(list);
    }

    /**
     * 根据 spuId 查询SpuImage信息
     * @param spuId
     * @return
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable("spuId") Long spuId){
        QueryWrapper<SpuImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id",spuId);
        List<SpuImage> list = spuImageService.list(queryWrapper);
        return Result.ok(list);
    }
}
