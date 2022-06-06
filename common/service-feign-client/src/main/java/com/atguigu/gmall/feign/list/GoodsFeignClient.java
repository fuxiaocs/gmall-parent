package com.atguigu.gmall.feign.list;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rpc/inner/es")
@FeignClient("service-list")
public interface GoodsFeignClient {


    @PostMapping("/goods/save")
    Result saveGoods(@RequestBody Goods goods);

    @DeleteMapping("/goods/{skuId}")
    Result deleteGoods(@PathVariable("skuId") Long skuId);

    @PostMapping("/goods/search")
    Result<GoodsSearchResultVo> searchGoods(@RequestBody SearchParam param);

    @PutMapping("/goods/updateScore/{skuId}")
    Result updateScoreBySkuId(@PathVariable("skuId") Long skuId,
                            @RequestParam("score") Long score);
}
