package com.atguigu.gmall.cart.rpc;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rpc/inner/cart")
public class RpcCartController {

    @Autowired
    CartService cartService;

    //skuId=51&skuNum=1&sourceType=query
    @GetMapping("/add/{skuId}")
    public Result addToCart(
            @PathVariable("skuId") String skuId,
            @RequestParam("skuNum") String skuNum){

        CartItemInfo cartItemInfo = cartService.addToCart(skuId,skuNum);

        return Result.ok(cartItemInfo);
    }

    @GetMapping("/deleteChecked")
    public Result deleteChecked(){
        cartService.deleteChecked();
        return Result.ok();
    }
}
