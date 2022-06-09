package com.atguigu.gmall.common.util;

import com.atguigu.gmall.model.to.CartUserIdTo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AuthUtil {

    private AuthUtil(){}

    public static CartUserIdTo getCartUserIdAndTempId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String userId = request.getHeader("UserId");
        String userTempId = request.getHeader("UserTempId");

        return new CartUserIdTo(userId,userTempId);
    }
}
