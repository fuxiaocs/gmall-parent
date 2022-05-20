package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/product")
@RestController
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    /**
     * 查询所有品牌
     */
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getBaseTrademarkList(){
        List<BaseTrademark> list = baseTrademarkService.list();
        return Result.ok(list);
    }

    /**
     * 分页查询 品牌
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/baseTrademark/{pageNum}/{pageSize}")
    public Result getBaseTrademarkByPage(
            @PathVariable("pageNum") Long pageNum,
            @PathVariable("pageSize") Long pageSize){
        Page<BaseTrademark> page = new Page<>(pageNum, pageSize);
        baseTrademarkService.findBaseTrademarkByPage(page);
        return Result.ok(page);
    }

    @PostMapping("/baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark){

        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    @GetMapping("/baseTrademark/get/{id}")
    public Result getBaseTrademarkById(@PathVariable("id") Long id){
        return Result.ok(baseTrademarkService.getById(id));
    }

    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result deleteBaseTrademark(@PathVariable("id") Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }
}
