package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.user.UserLoginResultVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.user.service.UserInfoService;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 登录信息判断
     * @param userInfo
     * @return
     */
    @Override
    public UserLoginResultVo login(UserInfo userInfo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",userInfo.getLoginName());
        queryWrapper.eq("passwd",MD5.encrypt(userInfo.getPasswd()));
        UserInfo userInfoResult = userInfoMapper.selectOne(queryWrapper);

        if (userInfoResult == null) {
            return null;
        }
        //设置请求对象ip
        userInfoResult.setIpAddr(userInfo.getIpAddr());
        String token = saveLoginInfoRedis(userInfoResult);


        return new UserLoginResultVo(token,userInfoResult.getNickName());
    }

    /**
     * 退出登录 从redis中移除 登录用户信息 根据token
     * @param token
     */
    @Override
    public void logout(String token) {
        stringRedisTemplate.delete(CacheConstant.LOGIN_USER_INFO + token);
    }

    /**
     * 将登录成功的数据保持在 redis中 并返回token
     * @param userInfoResult
     * @return
     */
    private String saveLoginInfoRedis(UserInfo userInfoResult) {
        String token = UUID.randomUUID().toString().replace("-","");
        stringRedisTemplate.opsForValue().set(
                CacheConstant.LOGIN_USER_INFO + token,
                JSONs.toStr(userInfoResult),
                7,
                TimeUnit.DAYS);
        return token;
    }
}




