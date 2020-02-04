package com.haipiao.common.interceptor;

import static com.haipiao.common.constant.LoggingConstant.ACTION_KPI;
import static com.haipiao.common.constant.LoggingConstant.REQUEST_UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put(REQUEST_UUID, UUID.randomUUID().toString());
        MDC.put(ACTION_KPI, request.getRequestURI());
        return true;
    }

}
