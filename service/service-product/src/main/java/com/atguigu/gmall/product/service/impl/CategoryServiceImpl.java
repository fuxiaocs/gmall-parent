package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.cache.service.RedisCacheService;
import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChild;
import com.atguigu.gmall.product.mapper.CategoryMapper;
import com.atguigu.gmall.product.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryAndChild> implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    RedisCacheService redisCacheService;

    /**
     * 获取所有分类与子分类
     *
     * @return
     */
    @Override
    public List<CategoryAndChild> getAllCategoryAndChild() {
        //先查 缓存
        List<CategoryAndChild> categoryAndChildList = redisCacheService.getCacheData(CacheConstant.CATEGORY_REDIS_KEY, new TypeReference<List<CategoryAndChild>>() {
        });
        //判断是否有
        if (categoryAndChildList == null) {
            //没有 去数据库查
            categoryAndChildList = categoryMapper.selectAllCategoryAndChild();
            //添加进缓存
            //可能为 null  可能有数据
            redisCacheService.save(CacheConstant.CATEGORY_REDIS_KEY, categoryAndChildList);
        }
        //返回数据
        return categoryAndChildList;
    }

    /**
     * 根据 skuId 获取分类层级
     *
     * @param skuId
     * @return
     */
    @Override
    public BaseCategoryView getBaseCategoryViewBySkuId(Long skuId) {
        return categoryMapper.selectBaseCategoryViewBySkuId(skuId);
    }
}
