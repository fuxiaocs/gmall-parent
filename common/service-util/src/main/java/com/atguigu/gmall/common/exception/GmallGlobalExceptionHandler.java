package com.atguigu.gmall.common.exception;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GmallGlobalExceptionHandler {

    @Value("${spring.application.name}")
    String serviceName;

    @ExceptionHandler(GmallException.class)
    public Result gmallException(GmallException e){
        log.error("业务异常 : {}",e);
        Result fail = Result.fail();
        fail.setCode(e.getCode());
        fail.setMessage(e.getMessage());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuffer url = request.getRequestURL();
        Map<String, String[]> params = request.getParameterMap();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("path",url);
        resultMap.put("params",params);
        resultMap.put("serviceName",serviceName);
        fail.setData(resultMap);

        return fail;
    }

    @ExceptionHandler(Exception.class)
    public Result systemException(Exception e){
        log.error("系统异常 : {}",e);
        Result fail = Result.fail();
        fail.setCode(500);
        fail.setMessage("系统异常");

        return fail;
    }
}
