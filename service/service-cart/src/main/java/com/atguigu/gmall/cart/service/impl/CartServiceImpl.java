package com.atguigu.gmall.cart.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
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
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Qualifier("otherThreadPool")
    @Autowired
    ThreadPoolExecutor otherThreadPool;

    /**
     * 将商品添加到购物车
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    @Override
    public CartItemInfo addToCart(String skuId, String skuNum) {
        //判断是使用 用户id 存储还是 临时id
        String redisKey = selectId();
        if (StringUtils.isEmpty(redisKey)) {
            throw new GmallException(ResultCodeEnum.CART_ID_ERROR);
        }
        //判断 购物车数量
        BoundHashOperations<String, String, String> userCart = getUserCart(redisKey);
        if (userCart.size() >= 200) {
            throw new GmallException(ResultCodeEnum.CART_NOT_SPACE);
        }

        //保存商品到购物车
        CartItemInfo cartItemInfo = saveToCart(skuId, skuNum, redisKey);

        //设置购物车超时时间
        //在 存储空间创建后设置超时才起效果
        setCartTimeOut(redisKey);

        return cartItemInfo;
    }

    /**
     * 获取对应用户的 购物车列表
     *
     * @return
     */
    @Override
    public List<CartItemInfo> getCartItemList() {
        String redisKey = selectId();

        //判断是否需要合并购物车
        //获取用户id  如果有值这代表  有用户登录
        String userId = getUserId();
        //获取临时id
        String userTempId = getUserTempId();

        //获取临时购物车
        BoundHashOperations<String, String, String> tempUserCart = getUserCart(userTempId);

        if (!StringUtils.isEmpty(userId) && tempUserCart != null && tempUserCart.size() > 0) { //用户登录了  临时购物车有数据 则需要合并
            //判断购物车 合并空间是否足够
            //获取用户购物车
            BoundHashOperations<String, String, String> userCart = getUserCart();

            if (userCart.size() + tempUserCart.size() > 200) {
                throw new GmallException(ResultCodeEnum.CART_MERGE_NOT_SPACE);
            }
//            //获取用户购物车
//            BoundHashOperations<String, String, String> userCart = getUserCart(userId);
            //将临时购物车商品拿出来 放入用户购物车     相当于给用户购物车添加商品
            tempUserCart.values()
                    .stream()
                    .forEach(item->{
                        CartItemInfo cartItemInfo = JSONs.strToObj(item, new TypeReference<CartItemInfo>() {
                        });
                        saveToCart(cartItemInfo.getId(),cartItemInfo.getSkuNum().toString(),userId);
                        tempUserCart.delete(cartItemInfo.getId());
                    });

        }
        //其余情况不需要合并
        // 登录+临时没数据    没登录    登录了没临时

//        BoundHashOperations<String, String, String> userCart = stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + redisKey);
        //自动决定拿的是 临时购物车还是用户购物车
        BoundHashOperations<String, String, String> userCart = getUserCart();
        isNullUserCartHandler(userCart);
        List<String> values = userCart.values();
        if (values == null || values.size() <= 0) {
            return null;
        }
        List<CartItemInfo> collect = values.stream()
                .map((value) -> JSONs.strToObj(value, new TypeReference<CartItemInfo>() {
                }))
                .sorted(((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime())))
                .collect(Collectors.toList());

        //异步执行修改价格
        otherThreadPool.submit(()->updateSkuRealPrice(userCart,collect));

        return collect;
    }

    /**
     * 修改商品实时价格
     * @param userCart
     * @param collect
     */
    private void updateSkuRealPrice(BoundHashOperations<String, String, String> userCart, List<CartItemInfo> collect) {
        for (CartItemInfo cartItemInfo : collect) {
            Result<BigDecimal> priceResult = productFeignClient.getSkuPriceBySkuId(cartItemInfo.getSkuId());
            if (priceResult.isOk()) {
                cartItemInfo.setSkuPrice(priceResult.getData());
                userCart.put(cartItemInfo.getId(),JSONs.toStr(cartItemInfo));
            }
        }
    }

//    /**
//     * 获取临时购物车
//     * @return
//     */
//    private BoundHashOperations<String, String, String> getTempUserCart() {
//        String tempCartId = getUserTempId();
//        if (StringUtils.isEmpty(tempCartId)) {
//            return null;
//        }
//
//        return stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + tempCartId);
//    }

    /**
     * 获取 临时id  没有返回null
     *
     * @return
     */
    private String getUserTempId() {
        CartUserIdTo cartUserIdAndTempId = AuthUtil.getCartUserIdAndTempId();
        String userTempId = cartUserIdAndTempId.getUserTempId();
        if (StringUtils.isEmpty(userTempId)) {
            return null;
        }
        return userTempId;
    }

    /**
     * 获取 用户id 获取不到返回null
     *
     * @return
     */
    private String getUserId() {
        CartUserIdTo cartUserIdAndTempId = AuthUtil.getCartUserIdAndTempId();
        String userId = cartUserIdAndTempId.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return userId;
    }

    /**
     * 不在原有基础上修改商品数量
     *
     * @param skuId
     * @param skuNum
     */
    @Override
    public void updateCartSkuNum(String skuId, String skuNum) {
//        String redisKey = selectId();
//        BoundHashOperations<String, String, String> userCart = stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + redisKey);
        BoundHashOperations<String, String, String> userCart = getUserCart();
        isNullUserCartHandler(userCart);


        String skuStr = userCart.get(skuId);
        CartItemInfo cartItemInfo = JSONs.strToObj(skuStr, new TypeReference<CartItemInfo>() {
        });
        cartItemInfo.setSkuNum(Integer.parseInt(skuNum));
        cartItemInfo.setUpdateTime(new Date());

        userCart.put(skuId, JSONs.toStr(cartItemInfo));
    }

    /**
     * 如果 购物车为null 报异常
     *
     * @param userCart
     */
    private void isNullUserCartHandler(BoundHashOperations<String, String, String> userCart) {
        if (userCart == null) {
            throw new GmallException(ResultCodeEnum.CART_ID_ERROR);
        }
    }

    /**
     * 删除购物车项 根据skuId
     *
     * @param skuId
     */
    @Override
    public void deleteCartItem(String skuId) {
        BoundHashOperations<String, String, String> userCart = getUserCart();
        isNullUserCartHandler(userCart);
        userCart.delete(skuId);
    }

    /**
     * 删除选中商品
     */
    @Override
    public void deleteChecked() {
        //获取购物车
        BoundHashOperations<String, String, String> userCart = getUserCart();
        isNullUserCartHandler(userCart);
        //所有商品信息
        List<String> values = userCart.values();
        if (values != null && values.size() > 0) {
            //选中商品的id
            Object[] skuArrays = values.stream()
                    .map(item -> JSONs.strToObj(item, new TypeReference<CartItemInfo>() {
                    }))
                    .filter(item -> item.getIsChecked() == 1)
                    .map(CartItemInfo::getId)
                    .toArray();
            // 有选中商品 才进行删除
            if (skuArrays != null && skuArrays.length > 0) userCart.delete(skuArrays);
        }
    }

    /**
     * 修改商品选中  和 不选中
     *
     * @param skuId
     * @param check
     */
    @Override
    public void checkCart(String skuId, Integer check) {
        //为1 或 0 时才执行
        if (check == 1 || check == 0) {
            BoundHashOperations<String, String, String> userCart = getUserCart();
            isNullUserCartHandler(userCart);
            String itemStr = userCart.get(skuId);
            if (!StringUtils.isEmpty(itemStr)) {
                CartItemInfo cartItemInfo = JSONs.strToObj(itemStr, new TypeReference<CartItemInfo>() {
                });
                cartItemInfo.setIsChecked(check);
                if (check == 1) {
                    cartItemInfo.setUpdateTime(new Date());
                }
                userCart.put(skuId, JSONs.toStr(cartItemInfo));
            }
        }
    }

    /**
     * 根据id 获取购物车
     *
     * @return
     */
    private BoundHashOperations<String, String, String> getUserCart(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + userId);
    }

    /**
     * 获取 用户的购物车  没登录就是临时购物车
     *
     * @return
     */
    private BoundHashOperations<String, String, String> getUserCart() {
        String redisKey = selectId();
        if (StringUtils.isEmpty(redisKey)) {
            return null;
        }
        BoundHashOperations<String, String, String> userCart = stringRedisTemplate.boundHashOps(CacheConstant.CART_KEY_PREFIX + redisKey);
        return userCart;
    }

    /**
     * 保存商品到购物车
     *
     * @param skuId
     * @param skuNum
     * @param redisKey
     * @return
     */
    private CartItemInfo saveToCart(String skuId, String skuNum, String redisKey) {
        String cartKey = CacheConstant.CART_KEY_PREFIX + redisKey;

        //redis 中 hash结构的存储
        BoundHashOperations<String, String, String> userCart = stringRedisTemplate.boundHashOps(cartKey);
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

        userCart.put(skuId, JSONs.toStr(cartItemInfo));

        return cartItemInfo;
    }

    private void setCartTimeOut(String cartKey) {
        // 获取用户id  如果拿不到就是没登录
        String userId = getUserId();
        if (userId == null) {
            stringRedisTemplate.expire(
                    CacheConstant.CART_KEY_PREFIX + cartKey,
                    Duration.ofDays(1));
        }
    }

    /**
     * 判断用 什么id作为redis 存储购物车的键
     *
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
