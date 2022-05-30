package com.atguigu.gmall.starter.cache.aop;

import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.starter.cache.service.RedisCacheService;
import com.atguigu.gmall.starter.constants.CacheConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AspectHelper {

    private SpelExpressionParser parser = new SpelExpressionParser();

    @Autowired
    private Map<String,RBloomFilter<Object>> bloomMap;

    @Autowired
    RedisCacheService redisCacheService;

    @Qualifier("skuIdBloom")
    @Autowired
    RBloomFilter bloomFilter;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ScheduledThreadPoolExecutor scheduledThreadPool;

    @Autowired
    StringRedisTemplate redisTemplate;

    public Object getCacheData(String cacheKey, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Type type = signature.getMethod().getGenericReturnType();
        return redisCacheService.getCacheData(
                cacheKey,
                new TypeReference<Object>() {
                    @Override
                    public Type getType() {
                        return type;
                    }
                });
    }

//    public boolean bloomExist(Object arg) {
//        return bloomFilter.contains(arg);
//    }

    public Boolean tryLock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        return lock.tryLock();
    }

    public void save(String cacheKey, Object resultData) {
        redisCacheService.save(cacheKey,resultData);
    }

    public void unlock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            if (lock.isLocked()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.info("redisson解锁异常 : {}",e);
        }
    }

    /**
     * 从Cache 注解中 获取 CacheKey
     * @param joinPoint
     * @return
     */
    public String getCacheKey(ProceedingJoinPoint joinPoint) {
        Cache cache = getCache(joinPoint);

        //如果 cacheKey为空 就取value
        String cacheKeySpel = StringUtils.isEmpty(cache.cacheKey())?cache.value():cache.cacheKey();

//        Expression expression = parser.parseExpression(cacheKeySpel, ParserContext.TEMPLATE_EXPRESSION);
//        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("args",joinPoint.getArgs());
//        String cacheKey = expression.getValue(context, String.class);

        return this.expressionParser(joinPoint, cacheKeySpel, String.class);
    }

    private Cache getCache(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

//        Cache cache = method.getAnnotation(Cache.class);
        return AnnotationUtils.findAnnotation(method, Cache.class);
    }

    /**
     * 获取 bloom名  获取不到这代表不使用布隆
     * @param joinPoint
     * @return
     */
    public String getBloomName(ProceedingJoinPoint joinPoint) {
        Cache cache = this.getCache(joinPoint);

        return cache.bloomFilterName();
    }

    /**
     * 动态 布隆 判断
     * @param bloomName
     * @param joinPoint
     * @return
     */
    public boolean bloomExist(String bloomName, ProceedingJoinPoint joinPoint) {
        Cache cache = this.getCache(joinPoint);
        String bloomValue = cache.bloomValue();

        RBloomFilter<Object> bloom = bloomMap.get(bloomName);

        Object value = this.expressionParser(joinPoint, bloomValue,Object.class);

        return bloom.contains(value);
    }

    private <T> T expressionParser(ProceedingJoinPoint joinPoint, String bloomValue,Class<T> clazz) {
        Expression expression = parser.parseExpression(bloomValue, ParserContext.TEMPLATE_EXPRESSION);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args",joinPoint.getArgs());

        return expression.getValue(context, clazz);
    }

    /**
     * 缓存 延迟双删
     * @param skuId
     */
    public void deleteCache(String skuId) {
        redisTemplate.delete(skuId);

        scheduledThreadPool.schedule(()->{
            redisTemplate.delete(skuId);
        },10, TimeUnit.SECONDS);
    }
}
