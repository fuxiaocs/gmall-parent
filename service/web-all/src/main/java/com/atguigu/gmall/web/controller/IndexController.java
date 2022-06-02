package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.to.CategoryAndChild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    ProductFeignClient productFeignClient;

    @GetMapping({"/","/index.html"})
    public String getIndexPage(Model model){
        Result<List<CategoryAndChild>> result = productFeignClient.getAllCategoryAndChild();
        if (result.isOk()){
            List<CategoryAndChild> list = result.getData();
            model.addAttribute("list",list);
        }
        return "index/index";
    }
}
