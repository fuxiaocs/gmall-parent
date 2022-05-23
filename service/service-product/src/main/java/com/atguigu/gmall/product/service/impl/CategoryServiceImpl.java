package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChild;
import com.atguigu.gmall.product.mapper.CategoryMapper;
import com.atguigu.gmall.product.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryAndChild> implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 获取所有分类与子分类
     * @return
     */
    @Override
    public List<CategoryAndChild> getAllCategoryAndChild() {
        return categoryMapper.selectAllCategoryAndChild();
    }

    /**
     * 根据 skuId 获取分类层级
     * @param skuId
     * @return
     */
    @Override
    public BaseCategoryView getBaseCategoryViewBySkuId(Long skuId) {
        return categoryMapper.selectBaseCategoryViewBySkuId(skuId);
    }
}
