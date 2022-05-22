package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.to.CategoryAndChild;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface CategoryMapper extends BaseMapper<CategoryAndChild> {

    List<CategoryAndChild> selectAllCategoryAndChild();
}
