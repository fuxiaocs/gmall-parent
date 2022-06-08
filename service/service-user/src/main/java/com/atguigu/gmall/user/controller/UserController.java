package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.user.UserLoginResultVo;
import com.atguigu.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo userInfo, HttpServletRequest request){
        //获取请求对象的ip
        String ipAddr = IpUtil.getIpAddress(request);
        userInfo.setIpAddr(ipAddr);

        UserLoginResultVo userLoginResultVo = userInfoService.login(userInfo);
        if (userLoginResultVo == null){
            return Result.build("", ResultCodeEnum.LOGIN_FAIL);
        }
        return Result.ok(userLoginResultVo);
    }

    @GetMapping("/passport/logout")
    public Result logout(@RequestHeader("token") String token){
        userInfoService.logout(token);
        return Result.ok();
    }
}
