package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.ItemDetailedTo;

public interface ItemService {
    ItemDetailedTo getItemDetailed(Long skuId);

    void updateHotScore(Long skuId);
}
