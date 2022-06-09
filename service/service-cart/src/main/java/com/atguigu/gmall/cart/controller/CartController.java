package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {

    @Autowired
    CartService cartService;

    ///api/cart/cartList
    @GetMapping("/cartList")
    public Result getCartItemList(){

        List<CartItemInfo> list = cartService.getCartItemList();

        return Result.ok(list);
    }
}
