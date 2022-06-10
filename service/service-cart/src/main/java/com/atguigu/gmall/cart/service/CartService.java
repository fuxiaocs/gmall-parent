package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartItemInfo;

import java.util.List;

public interface CartService {
    CartItemInfo addToCart(String skuId, String skuNum);

    List<CartItemInfo> getCartItemList();

    /**
     * 不在原有基础上修改商品数量
     * @param skuId
     * @param skuNum
     */
    void updateCartSkuNum(String skuId, String skuNum);

    /**
     * 删除购物车项 根据skuId
     * @param skuId
     */
    void deleteCartItem(String skuId);

    /**
     * 删除选中的商品
     */
    void deleteChecked();

    /**
     * 修改选中与不选择商品 1选中 0不选中
     * @param skuId
     * @param check
     */
    void checkCart(String skuId, Integer check);
}
