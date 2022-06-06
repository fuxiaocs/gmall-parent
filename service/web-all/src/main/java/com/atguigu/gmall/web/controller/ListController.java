package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.list.GoodsFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ListController {

    @Autowired
    GoodsFeignClient goodsFeignClient;

    @GetMapping("/list.html")
    public String getListPage(SearchParam param, Model model){

        Result<GoodsSearchResultVo> goodsVoResult = goodsFeignClient.searchGoods(param);

        if (goodsVoResult.isOk()) {
            GoodsSearchResultVo goodsVo = goodsVoResult.getData();
            model.addAttribute("searchParam",goodsVo.getSearchParam());
            model.addAttribute("propsParamList",goodsVo.getPropsParamList());
            model.addAttribute("trademarkParam",goodsVo.getTrademarkParam());
            model.addAttribute("urlParam",goodsVo.getUrlParam());
            model.addAttribute("trademarkList",goodsVo.getTrademarkList());
            model.addAttribute("attrsList",goodsVo.getAttrsList());
            model.addAttribute("orderMap",goodsVo.getOrderMap());
            model.addAttribute("goodsList",goodsVo.getGoodsList());
            model.addAttribute("pageNo",goodsVo.getPageNo());
            model.addAttribute("totalPages",goodsVo.getTotalPages());
        }

        return "list/index";
    }
}
