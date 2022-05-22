package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryAndChild;
import com.atguigu.gmall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rpc/inner/product")
public class RpcCategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 获取所有分类与子分类
     * @return
     */
    @GetMapping("/getAllCategoryAndChild")
    public Result getAllCategoryAndChild(){
        List<CategoryAndChild> list = categoryService.getAllCategoryAndChild();
        return Result.ok(list);
    }
}
