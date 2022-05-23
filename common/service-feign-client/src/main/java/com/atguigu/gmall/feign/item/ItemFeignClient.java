package com.atguigu.gmall.feign.item;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.ItemDetailedTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@FeignClient("service-item")
@RequestMapping("/rpc/inner/item")
public interface ItemFeignClient {


    /**
     * 根据 skuId 获取商品详细信息
     * @param skuId
     * @return
     */
    @GetMapping("/getItemDetailed/{skuId}")
    Result<ItemDetailedTo> getItemDetailed(@PathVariable("skuId") Long skuId);

}
