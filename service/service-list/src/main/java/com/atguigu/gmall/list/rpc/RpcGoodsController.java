package com.atguigu.gmall.list.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.service.GoodsEsService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rpc/inner/es")
public class RpcGoodsController {

    @Autowired
    GoodsEsService goodsEsService;

    @PostMapping("/goods")
    public Result saveGoods(@RequestBody Goods goods){

        goodsEsService.saveGoods(goods);

        return Result.ok();
    }


    @DeleteMapping("/goods/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId){

        goodsEsService.deleteGoods(skuId);

        return Result.ok();
    }


    @GetMapping("/goods")
    public Result<GoodsSearchResultVo> searchGoods(@RequestBody SearchParam param){

        GoodsSearchResultVo goodsVo = goodsEsService.searchGoods(param);

        return Result.ok(goodsVo);
    }
}
