package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChild;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper extends BaseMapper<CategoryAndChild> {

    List<CategoryAndChild> selectAllCategoryAndChild();

    BaseCategoryView selectBaseCategoryViewBySkuId(@Param("skuId") Long skuId);
}
