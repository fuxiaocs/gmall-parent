package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShardingSphereTest {

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Test
    public void insertTest(){
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setSpuName("fxcs");
        spuInfoMapper.insert(spuInfo);
    }

    @Test
    public void readTest(){
        System.out.println(spuInfoMapper.selectById(29));
        System.out.println(spuInfoMapper.selectById(29));
        System.out.println(spuInfoMapper.selectById(29));
        System.out.println(spuInfoMapper.selectById(29));
    }
}
