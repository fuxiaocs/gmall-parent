package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.cart.CartItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    @Autowired
    CartFeignClient cartFeignClient;
    ///addCart.html?skuId=51&skuNum=1&sourceType=query
    @GetMapping("/addCart.html")
    public String toAddCartPage(
            @RequestParam("skuId") String skuId,
            @RequestParam("skuNum") String skuNum,
            Model model){

        Result<CartItemInfo> cartItemInfoResult = cartFeignClient.addToCart(skuId, skuNum);
        if (cartItemInfoResult.isOk()) {
            CartItemInfo data = cartItemInfoResult.getData();
            model.addAttribute("skuInfo", data);
            model.addAttribute("skuNum", data.getSkuNum());
        }


        return "cart/addCart";
    }

    @GetMapping("/cart.html")
    public String toCartPage(){

        return "cart/index";
    }

    ///cart/deleteChecked
    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){
        cartFeignClient.deleteChecked();
        return "cart/index";
    }
}
