package com.bil.spring.web;

import com.bil.spring.model.Response;
import com.bil.spring.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 执行时机：在Controller执行return方式之后，在response返回给浏览器之前，执行的对response的一些处理。
 * 实现功能：可以实现对response数据的一些统一封装或者加密等操作。
 *
 * @author haibo.yang
 * @since 2022/1/10
 */
@Slf4j
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    public static final ConcurrentMap<String, Boolean> CACHE = new ConcurrentHashMap<>();


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = methodParameter.getMethod();
        String cacheKey = method.toString();
        Boolean isSupport = CACHE.get(cacheKey);
        if (null != isSupport) {
            return isSupport;
        }
        if (method.isAnnotationPresent(Response.class)) {
            isSupport = true;
        } else if (method.getClass().isAnnotationPresent(Response.class)) {
            isSupport = true;
        } else {
            isSupport = false;
        }
        CACHE.put(cacheKey, isSupport);
        return isSupport;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (body instanceof ResponseResult) {
            return body;
        }
        return ResponseResult.success(body);
    }
}
