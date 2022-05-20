package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark>
    implements BaseTrademarkService{

    @Autowired
    BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public Page<BaseTrademark> findBaseTrademarkByPage(Page<BaseTrademark> page) {
        baseTrademarkMapper.selectPage(page,null);
        return page;
    }
}




