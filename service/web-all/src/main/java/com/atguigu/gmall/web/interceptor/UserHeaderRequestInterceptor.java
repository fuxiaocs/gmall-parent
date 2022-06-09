package com.atguigu.gmall.web.interceptor;

import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.model.to.CartUserIdTo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserHeaderRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        CartUserIdTo cartUserIdTo = AuthUtil.getCartUserIdAndTempId();

        requestTemplate.header("UserId",cartUserIdTo.getUserId());
        requestTemplate.header("UserTempId",cartUserIdTo.getUserTempId());
    }
}
