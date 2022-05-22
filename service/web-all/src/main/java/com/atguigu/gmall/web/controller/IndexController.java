package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryAndChild;
import com.atguigu.gmall.web.rpc.CategoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    CategoryFeign categoryFeign;

    @GetMapping("/")
    public String getIndexPage(Model model){
        Result<List<CategoryAndChild>> result = categoryFeign.getAllCategoryAndChild();
        if (result.isOk()){
            List<CategoryAndChild> list = result.getData();
            model.addAttribute("list",list);
        }
        return "index/index";
    }
}
