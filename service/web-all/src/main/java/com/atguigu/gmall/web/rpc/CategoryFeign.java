package com.atguigu.gmall.web.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryAndChild;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-product")
@RequestMapping("rpc/inner/product")
public interface CategoryFeign {

    /**
     * 获取所有分类与子分类
     * @return
     */
    @GetMapping("/getAllCategoryAndChild")
    Result<List<CategoryAndChild>> getAllCategoryAndChild();
}
