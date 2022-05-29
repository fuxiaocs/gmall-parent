package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class CategoryController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @Autowired
    private BaseCategory2Service baseCategory2Service;

    @Autowired
    private BaseCategory3Service baseCategory3Service;


    @GetMapping("/getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> list = baseCategory1Service.getAllCategory1();
        return Result.ok(list);
    }


    @GetMapping("/getCategory2/{c1Id}")
    public Result getCategory2ByC1Id(@PathVariable("c1Id") Long c1Id){
        List<BaseCategory2> list = baseCategory2Service.getCategory2ByC1Id(c1Id);
        return Result.ok(list);
    }


    @GetMapping("/getCategory3/{c2Id}")
    public Result getCategory3ByC2Id(@PathVariable("c2Id") Long c2Id){
        List<BaseCategory3> list = baseCategory3Service.getCategory3ByC2Id(c2Id);
        return Result.ok(list);
    }

}
