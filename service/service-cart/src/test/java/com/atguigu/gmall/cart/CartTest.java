package com.atguigu.gmall.cart;

import com.atguigu.gmall.common.constants.CacheConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@SpringBootTest
public class CartTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void boundHashTest(){
        String id = "null";
        BoundHashOperations<String, String, String> cart = getCart(id);
//        stringRedisTemplate.expire(CacheConstant.CART_KEY_PREFIX + id,Duration.ofDays(1));
        cart.put("112233","112233");
        cart.expire(Duration.ofDays(1));
        System.out.println("cart = " + cart);
    }

    BoundHashOperations<String, String, String> getCart(String id){
        return stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + id);
    }
}
