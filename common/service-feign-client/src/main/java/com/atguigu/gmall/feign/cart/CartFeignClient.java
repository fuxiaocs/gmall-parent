package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItemInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-cart")
@RequestMapping("rpc/inner/cart")
public interface CartFeignClient {

    @GetMapping("/add/{skuId}")
    Result<CartItemInfo> addToCart(
            @PathVariable("skuId") String skuId,
            @RequestParam("skuNum") String skuNum);

    @GetMapping("/deleteChecked")
    Result deleteChecked();
}
