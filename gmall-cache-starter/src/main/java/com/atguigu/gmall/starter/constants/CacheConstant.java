package com.atguigu.gmall.starter.constants;

import java.util.concurrent.TimeUnit;

public class CacheConstant {

    public static final String LOCK_PREFIX = "lock";
    public static final String BLOOM_SKU_ID = "bloom:skuid"; //skuid 布隆在redis中的名字
    public static final String CACHE_NULL = "null"; //不存在数据 缓存内容
    public static final Long EXIST_CACHE_TIME = 3L; //存在数据 缓存时间

    public static final TimeUnit EXIST_CACHE_UNIT = TimeUnit.DAYS; //存在数据 缓存单位

    public static final Long NULL_CACHE_TIME = 30L;  //不存在数据 缓存时间

    public static final TimeUnit NULL_CACHE_UNIT = TimeUnit.MINUTES; //不存在数据 缓存单位
}
