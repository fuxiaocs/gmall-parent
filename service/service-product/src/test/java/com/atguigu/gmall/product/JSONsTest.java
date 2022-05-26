package com.atguigu.gmall.product;


import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONsTest {

    @Test
    public void nullJsonTest(){
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//        String s = JSONs.toStr(baseAttrInfo);
//        System.out.println("s = " + s);
//
//        System.out.println(ObjectUtils.isEmpty(baseAttrInfo));
//        System.out.println(baseAttrInfo == null);
//
//        List<Object> list = new ArrayList<>();
//        String s = JSONs.toStr(list);
//        System.out.println("s = " + s);

//        Object o = new Object();
//        String s1 = JSONs.toStr(o);
//        System.out.println("s1 = " + s1);

//        String s = JSONs.toStr(null);
//        System.out.println("s = " + s);

//        String s = new String();
//        String s1 = JSONs.toStr(s);
//        System.out.println("s1 = " + s1);

//        HashMap<Object, Object> map = new HashMap<>();
//        String s = JSONs.toStr(map);
//        System.out.println("s = " + s);
//
//        Long[] strings = {};
//        String s1 = JSONs.toStr(strings);
//        System.out.println("s1 = " + s1);
        Funnel<Integer> funnel = Funnels.integerFunnel();
        BloomFilter<Integer> bloom = BloomFilter.create(funnel, 500000, 0.9);

        bloom.put(70);
        bloom.put(77);
        bloom.put(777);

        System.out.println(bloom.mightContain(70));
        System.out.println(bloom.mightContain(7));
        System.out.println(bloom.mightContain(70));
        System.out.println(bloom.mightContain(7777));


    }
}
