package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChild;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService extends IService<CategoryAndChild> {

    List<CategoryAndChild> getAllCategoryAndChild();

    BaseCategoryView getBaseCategoryViewBySkuId(Long skuId);
}
