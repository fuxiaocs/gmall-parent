package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.constants.CacheConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.gateway.properties.AuthProperties;
import com.atguigu.gmall.model.user.UserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class UserAuthFilter implements GlobalFilter {

    @Autowired
    AuthProperties authProperties;

    //ant风格 路径匹配器
    AntPathMatcher matcher = new AntPathMatcher();

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        //直接放行的路径
        List<String> anyoneurls = authProperties.getAnyoneurls();
        for (String anyoneurl : anyoneurls) {
            boolean match = matcher.match(anyoneurl, path);
            if (match) { //匹配 放行
                return chain.filter(exchange);
            }
        }

        //禁止外部访问的路径
        List<String> unanyoneurls = authProperties.getUnanyoneurls();
        for (String unanyoneurl : unanyoneurls) {
            boolean match = matcher.match(unanyoneurl, path);
            if (match) { //匹配  禁止访问
                String str = JSONs.toStr(Result.build("", ResultCodeEnum.PERMISSION));
                DataBuffer wrap = exchange.getResponse()
                        .bufferFactory()
                        .wrap(str.getBytes(StandardCharsets.UTF_8));

                Mono<DataBuffer> just = Mono.just(wrap);

                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

                return exchange.getResponse().writeWith(just);
            }
        }

        //需要登录才能访问
        List<String> authurls = authProperties.getAuthurls();
        for (String authurl : authurls) {
            boolean match = matcher.match(authurl, path);
            if (match) { //需要登录才能访问
                //判断是否带token
                String token = existToken(exchange);
                if (!StringUtils.isEmpty(token)) { //带有存在
                    //检查带的token是否正确 正确返回token 不正确返回null
                    token = checkToken(token,exchange);
                    if (!StringUtils.isEmpty(token)) { //token不为空 代表token正确
                        // userId透传 放行
                        ServerWebExchange newExchange = mutateExchange(exchange,token);
                        return chain.filter(newExchange);
                    }
                }
                //没带token 回调到登录页 || token错误
                return toLoginPage(exchange);
            }
        }

        //普通请求
        //没带token  和 带对token 都可以 放行
        //但是带错token 需要重新登录
        String token = existToken(exchange);
        if (token != null) { //带了 token
            token = checkToken(token, exchange);
            if ( token == null) {// 是错的
                //需要返回登录页 重新登录
                return toLoginPage(exchange);
            }
            // token 是对的 userId透传
            ServerWebExchange newExchange = mutateExchange(exchange,token);
            return chain.filter(newExchange);
        }

        //普通请求没有token  放行
        //没带token 只透传 UserTempId
        return chain.filter(mutateExchange(exchange));
    }

    /**
     * 没带token  只透传 UserTempId
     * @param exchange
     * @return
     */
    private ServerWebExchange mutateExchange(ServerWebExchange exchange) {

        return exchange.mutate()
                .request(exchange.getRequest()
                        .mutate()
                        .header("UserTempId", getUserTempId(exchange))
                        .build())
                .response(exchange.getResponse())
                .build();
    }

    /**
     * 克隆Exchange
     * @param exchange
     * @param token
     * @return
     */
    private ServerWebExchange mutateExchange(ServerWebExchange exchange,String token) {
        //根据token 从redis中 获取userInfo
        UserInfo userInfo = JSONs.strToObj(
                stringRedisTemplate.opsForValue()
                        .get(CacheConstant.LOGIN_USER_INFO + token),
                new TypeReference<UserInfo>() {
                }
        );

        String userTempId = getUserTempId(exchange);
        //克隆的 请求对象
        ServerHttpRequest newRequest = exchange.getRequest()
                .mutate()
                .header("UserId",userInfo.getId().toString())
                .header("UserTempId",userTempId)
                .build();
//        newRequest.getHeaders().add("userId",userInfo.getId().toString()); //错误的 不能使用

        //克隆的 ServerWebExchange对象
        return exchange
                .mutate()
                .request(newRequest)
                .response(exchange.getResponse())
                .build();
    }

    /**
     * 获取 临时购物车Id
     * @param exchange
     * @return
     */
    private String getUserTempId(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        String userTempId = null;
        if (cookies != null) {
            HttpCookie userTempIdCookie = cookies.getFirst("userTempId");
            if (userTempIdCookie != null) {
                userTempId = userTempIdCookie.getValue();
            } else {
                userTempId = request.getHeaders().getFirst("userTempId");
            }
        }

        if (StringUtils.isEmpty(userTempId)) {
            return null;
        }

        return userTempId;
    }

    /**
     * 回调到登录页
     * @param exchange
     * @return
     */
    private Mono toLoginPage(ServerWebExchange exchange) {
        //响应式编程 请求与响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //给响应对象 添加token到cookie里
        //重新设置token 时间为0 设置作用域
        ResponseCookie tokenCookie = ResponseCookie.from("token", "fuxiaocs")
                .maxAge(0L)
                .domain(".gmall.com")
                .build();
        response.addCookie(tokenCookie);

        //设置重定向 三个设置 状态码  请求头 获取Mono对象
        //设置 响应状态码
        response.setStatusCode(HttpStatus.FOUND);
        //设置 重定向 地址
        //获取原始 页面地址
        String originalPath = request.getURI().toString();
        //登录页 地址
        //http://passport.gmall.com/login.html?originUrl=http://gmall.com/
        URI loginUri = URI.create(authProperties.getLoginpage() + "?originUrl=" + originalPath);
        response.getHeaders().setLocation(loginUri);
        //获取Mono对象 setComplete
        return response.setComplete();
    }

    /**
     * 判断token 是否正确
     * 能否在redis中取到对应数据
     * 对应数据ip地址是否和这次ip地址相同
     * @param token
     * @param exchange
     * @return
     */
    private String checkToken(String token, ServerWebExchange exchange) {
        String ipAddr = IpUtil.getGatwayIpAddress(exchange.getRequest());
        String userInfoJson = stringRedisTemplate.opsForValue()
                .get(CacheConstant.LOGIN_USER_INFO + token);
        // 从redis中没有找到 对应数据 返回null
        if (userInfoJson == null) {
            return null;
        }

        UserInfo userInfo = JSONs.strToObj(userInfoJson, new TypeReference<UserInfo>() {
        });

        // 从redis中找到的数据ip 与这次请求ip不同
        if (!ipAddr.equals(userInfo.getIpAddr())) {
            return null;
        }

        return token;
    }

    /**
     * 从cookie 或 请求头中 拿token 不判断对错
     * @param exchange
     * @return
     */
    private String existToken(ServerWebExchange exchange) {
        //获取请求中的cookie
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();

        String token = null;
        if (cookies != null) { //cookie 不为空 尝试从中取出token
            HttpCookie cookieToken = cookies.getFirst("token");
            if (cookieToken != null) {
                token = cookieToken.getValue();
            } else { //从cookie 中没有取到token
                //尝试从请求头中取token
                token = exchange.getRequest().getHeaders().getFirst("token");
            }
        }

        //可能会取到 "" 空字符
        if (StringUtils.isEmpty(token)) {
            return null;
        }
//        if (StringUtils.isEmpty(token)) { //从cookie和请求头中都取不出 token
//            return null;
//        }
        // 取出了token
        return token;
    }
}
