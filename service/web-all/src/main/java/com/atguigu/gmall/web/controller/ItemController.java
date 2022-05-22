package com.atguigu.gmall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    @GetMapping("/{skuId}.html")
    public String getItemPage(@PathVariable("skuId") Long skuId, Model model){

        return "item/index";
    }
}
