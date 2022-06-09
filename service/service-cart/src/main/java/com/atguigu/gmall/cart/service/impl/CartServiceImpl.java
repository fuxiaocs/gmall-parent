package com.atguigu.gmall.cart.service.impl;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.cart.CartItemInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.CartUserIdTo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 将商品添加到购物车
     * @param skuId
     * @param skuNum
     * @return
     */
    @Override
    public CartItemInfo addToCart(String skuId, String skuNum) {
        //判断是使用 用户id 存储还是 临时id
        String redisKey = selectId();


        //保存商品到购物车
        CartItemInfo cartItemInfo = saveToCart(skuId,skuNum,redisKey);


        return cartItemInfo;
    }

    /**
     * 获取对应用户的 购物车列表
     * @return
     */
    @Override
    public List<CartItemInfo> getCartItemList() {
        String redisKey = selectId();

        BoundHashOperations<String, String, String> userCart = stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + redisKey);

        List<String> values = userCart.values();
        return values.stream()
                .map((value)-> JSONs.strToObj(value, new TypeReference<CartItemInfo>() {}))
                .sorted(((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime())))
                .collect(Collectors.toList());
    }

    /**
     * 保存商品到购物车
     * @return
     * @param skuId
     * @param skuNum
     * @param redisKey
     */
    private CartItemInfo saveToCart(String skuId, String skuNum, String redisKey) {
        //redis 中 hash结构的存储
        BoundHashOperations<String, String, String> userCart = stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + redisKey);

        CartItemInfo cartItemInfo = new CartItemInfo();

        //判断是添加 还是新增
        Boolean exist = userCart.hasKey(skuId);
        //拿不到代表 新增
        if (!exist) {
            //根据skuId 查询到商品信息
            Result<SkuInfo> skuInfoResult = productFeignClient.getSkuInfoBySkuId(Long.parseLong(skuId));
            if (skuInfoResult.isOk()) {
                SkuInfo data = skuInfoResult.getData();

                cartItemInfo.setUserId(redisKey);
                cartItemInfo.setSkuId(Long.parseLong(skuId));
                cartItemInfo.setCartPrice(data.getPrice());
                cartItemInfo.setSkuNum(Integer.valueOf(skuNum));
                cartItemInfo.setSkuDefaultImg(data.getSkuDefaultImg());
                cartItemInfo.setSkuName(data.getSkuName());
//                cartItemInfo.setIsChecked(0);
                cartItemInfo.setCreateTime(new Date());
                cartItemInfo.setUpdateTime(new Date());
                cartItemInfo.setSkuPrice(data.getPrice());
                cartItemInfo.setId(skuId);
            }
        } else {
            String skuInfoStr = userCart.get(skuId);
            cartItemInfo = JSONs.strToObj(skuInfoStr, new TypeReference<CartItemInfo>() {
            });
            cartItemInfo.setSkuNum(cartItemInfo.getSkuNum() + Integer.parseInt(skuNum));
            cartItemInfo.setUpdateTime(new Date());
        }

        userCart.put(skuId,JSONs.toStr(cartItemInfo));

        return cartItemInfo;
    }

    /**
     * 判断用 什么id作为redis 存储购物车的键
     * @return
     */
    private String selectId() {
        //通过自定义工具类  获取透传的 用户id 和 临时id
        CartUserIdTo cartUserIdAndTempId = AuthUtil.getCartUserIdAndTempId();
        //如果用户id 不为空 就用用户id
        if (!StringUtils.isEmpty(cartUserIdAndTempId.getUserId())) {
            return cartUserIdAndTempId.getUserId();
        } else {
            return cartUserIdAndTempId.getUserTempId();
        }
    }
}
