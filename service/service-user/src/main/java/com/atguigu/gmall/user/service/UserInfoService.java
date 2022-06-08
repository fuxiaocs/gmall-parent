package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.user.UserLoginResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface UserInfoService extends IService<UserInfo> {

    UserLoginResultVo login(UserInfo userInfo);

    void logout(String token);
}
