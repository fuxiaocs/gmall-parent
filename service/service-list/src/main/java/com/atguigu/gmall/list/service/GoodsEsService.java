package com.atguigu.gmall.list.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;

public interface GoodsEsService {
    void saveGoods(Goods goods);

    void deleteGoods(Long skuId);

    GoodsSearchResultVo searchGoods(SearchParam param);
}
