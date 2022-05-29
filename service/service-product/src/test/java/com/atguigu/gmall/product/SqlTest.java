package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

@SpringBootTest
public class SqlTest {

    @Autowired
    SkuInfoService skuInfoService;

    @Test
    public void skuInfoTest(){
        SkuInfo skuInfo = new SkuInfo();
        System.out.println("skuInfo -- null --" + (skuInfo == null));
        System.out.println("skuInfo : ObjectUtils :" + ObjectUtils.isEmpty(skuInfo));
        SkuInfo skuInfoBySkuId = skuInfoService.getSkuInfoBySkuId(100L);
        System.out.println("skuInfoBySkuId = " + skuInfoBySkuId);
        System.out.println("null -- " + (skuInfoBySkuId == null));
        System.out.println("ObjectUtils : " + ObjectUtils.isEmpty(skuInfoBySkuId));
    }
}
