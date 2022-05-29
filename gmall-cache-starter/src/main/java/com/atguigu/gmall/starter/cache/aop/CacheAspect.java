package com.atguigu.gmall.starter.cache.aop;

import com.atguigu.gmall.starter.constants.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Aspect
//@Component
public class CacheAspect {

    @Autowired
    AspectHelper aspectHelper;

    @Around("@annotation(com.atguigu.gmall.starter.cache.aop.annotation.Cache)")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        //缓存键
//        String cacheKey = CacheConstant.SKU_CACHE_KEY_PREFIX + args[0];
        String cacheKey = aspectHelper.getCacheKey(joinPoint);
        //查询缓存
        Object resultData = aspectHelper.getCacheData(cacheKey, joinPoint);

        if (ObjectUtils.isEmpty(resultData)) {//缓存不存在
            log.info("缓存未命中");

            //获取 bloom容器名
            String bloomName = aspectHelper.getBloomName(joinPoint);

            //锁名称
            String lockName = CacheConstant.LOCK_PREFIX + cacheKey;

            //如果 获取的bloom名为空 则表示 不用bloom
            if (StringUtils.isEmpty(bloomName)) {//不使用布隆
                return findDatabase(joinPoint, args, cacheKey, lockName);
            } else { // 使用布隆
                //询问bloom
                if (aspectHelper.bloomExist(bloomName,joinPoint)) {//bloom 觉得存在
                    log.info("bloom觉得存在");
                    return findDatabase(joinPoint, args, cacheKey, lockName);
                }
                //bloom说不存在 返回null
                log.info("bloom说不存在");
                return null;
            }
        }
        //缓存存在 返回
        log.info("缓存命中");
        return resultData;
    }

    /**
     *  查询 数据库
     * @param joinPoint
     * @param args
     * @param cacheKey
     * @param lockName
     * @return
     */
    private Object findDatabase(ProceedingJoinPoint joinPoint, Object[] args, String cacheKey, String lockName) {
        Object resultData;//抢锁
        Boolean lock = aspectHelper.tryLock(lockName);
        if (lock) {//抢到锁的查询数据库
            try {
                log.info("抢到锁,查询数据库");
                resultData = joinPoint.proceed(args);
                //缓存数据
                aspectHelper.save(cacheKey, resultData);
                //返回数据
                return resultData;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                //解锁
                aspectHelper.unlock(lockName);
            }
        }
        // 没抢到锁
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("没抢到锁,等待查询缓存");
        return aspectHelper.getCacheData(cacheKey, joinPoint);
    }
}
