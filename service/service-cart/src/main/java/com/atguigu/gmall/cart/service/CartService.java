package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartItemInfo;

import java.util.List;

public interface CartService {
    CartItemInfo addToCart(String skuId, String skuNum);

    List<CartItemInfo> getCartItemList();
}
