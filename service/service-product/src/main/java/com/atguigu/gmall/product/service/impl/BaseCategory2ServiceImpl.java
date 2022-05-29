package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseCategory2ServiceImpl extends ServiceImpl<BaseCategory2Mapper, BaseCategory2> implements BaseCategory2Service {

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Cache("category2:#{#args[0]}")
    @Override
    public List<BaseCategory2> getCategory2ByC1Id(Long c1Id) {
        QueryWrapper<BaseCategory2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category1_id",c1Id);
        return baseCategory2Mapper.selectList(queryWrapper);
    }
}
