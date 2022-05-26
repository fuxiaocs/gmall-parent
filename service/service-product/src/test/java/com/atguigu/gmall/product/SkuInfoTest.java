package com.atguigu.gmall.product;

import com.atguigu.gmall.product.service.SkuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SkuInfoTest {

    @Autowired
    SkuInfoService skuInfoService;

    @Test
    public void skuIdTest(){
        List<Long> allSkuId = skuInfoService.getAllSkuId();
        System.out.println(allSkuId);
    }
}
