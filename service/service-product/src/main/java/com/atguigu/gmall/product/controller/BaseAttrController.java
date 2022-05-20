package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分类属性")
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {

    @Autowired
    BaseAttrInfoService baseAttrInfoService;

    @Autowired
    BaseAttrValueService baseAttrValueService;

    @ApiOperation("分类查询")
    @GetMapping("/attrInfoList/{c1Id}/{c2Id}/{c3Id}")
    public Result getAttrInfoAndValueByCategoryId(
            @PathVariable("c1Id") Long c1Id,
            @PathVariable("c2Id") Long c2Id,
            @PathVariable("c3Id") Long c3Id){
        List<BaseAttrInfo> list = baseAttrInfoService.findAttrInfoAndValueByCategoryId(c1Id,c2Id,c3Id);
        return Result.ok(list);
    }

    @ApiOperation("增加分类")
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        baseAttrInfoService.saveOrUpdateAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(
            @PathVariable("attrId") Long attrId){
        List<BaseAttrValue> list = baseAttrValueService.findAttrValueListByAttrId(attrId);
        return Result.ok(list);
    }

}
