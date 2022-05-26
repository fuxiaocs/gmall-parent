package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONs {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JSONs(){ }

    public static String toStr(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON转字符串异常:{}",e);
        }
        return null;
    }

    public static <T> T strToObj(String cacheData, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(cacheData, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T nullInstance(TypeReference<T> typeReference) {
        T t = null;
        try {
            t = objectMapper.readValue("[]", typeReference);
        } catch (JsonProcessingException e) {
            try {
                log.info("typeReference类型不是集合 :{} ",typeReference);
                t = objectMapper.readValue("{}",typeReference);
            } catch (JsonProcessingException ex) {
                log.error("准备空实例异常 : {}",ex);
            }
        }
        return t;
    }
}
