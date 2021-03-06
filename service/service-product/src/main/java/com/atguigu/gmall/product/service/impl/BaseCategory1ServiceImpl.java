package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1> implements BaseCategory1Service {

    @Autowired
    BaseCategory1Mapper baseCategory1Mapper;

    /**
     * 获取所有一级分类
     * @return
     */
    @Cache("category1")
    @Override
    public List<BaseCategory1> getAllCategory1() {
        return baseCategory1Mapper.selectList(null);
    }
}
