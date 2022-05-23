package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChild;
import com.atguigu.gmall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 根据 skuId 获取分类层级
     * @param skuId
     * @return
     */
    @GetMapping("/getBaseCategoryViewBySkuId/{skuId}")
    public Result getBaseCategoryViewBySkuId(
            @PathVariable("skuId") Long skuId){
        BaseCategoryView baseCategoryView = categoryService.getBaseCategoryViewBySkuId(skuId);
        return Result.ok(baseCategoryView);
    }
}
