package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface BaseTrademarkService extends IService<BaseTrademark> {

    /**
     * 分页查询 品牌信息
     * @param page
     */
    Page<BaseTrademark> findBaseTrademarkByPage(Page<BaseTrademark> page);
}
