package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItemInfo;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {

    @Autowired
    CartService cartService;

    ///cartList
    @GetMapping("/cartList")
    public Result getCartItemList(){

        List<CartItemInfo> list = cartService.getCartItemList();

        return Result.ok(list);
    }

    ///addToCart/51/-1
    @PostMapping("/addToCart/{skuId}/{skuNum}")
    public Result addToCart(
            @PathVariable("skuId") String skuId,
            @PathVariable("skuNum") String skuNum){
        cartService.addToCart(skuId,skuNum);
        return Result.ok();
    }

    ///checkCart/43/1
    @GetMapping("/checkCart/{skuId}/{check}")
    public Result checkCart(
            @PathVariable("skuId") String skuId,
            @PathVariable("check") Integer check){
        cartService.checkCart(skuId, check);
        return Result.ok();
    }

    ///deleteCart/51
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCartItem(@PathVariable("skuId") String skuId){
        cartService.deleteCartItem(skuId);
        return Result.ok();
    }
}
