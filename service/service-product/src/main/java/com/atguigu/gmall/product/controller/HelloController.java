package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/product")
@RestController
public class HelloController {

    @GetMapping("/hello/{num}")
    public String hello(@PathVariable Long num){
        Long i = 10/num;
        if (num % 2 == 0){
            throw new GmallException(ResultCodeEnum.COUPON_GET);
        }
        return "hello";
    }
}
