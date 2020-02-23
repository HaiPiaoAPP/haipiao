package com.haipiao.common.interceptor;

import static com.haipiao.common.constant.LoggingConstant.ACTION_KPI;
import static com.haipiao.common.constant.LoggingConstant.REQUEST_UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!StringUtils.equalsIgnoreCase("/error", request.getRequestURI())) {
            MDC.put(REQUEST_UUID, UUID.randomUUID().toString());
            MDC.put(ACTION_KPI, request.getRequestURI());
            logger.info("\n-------- Request --------\n"
                    + "{} {} {}\n"
                    + "{}"
                    + "-------------------------\n",
                request.getMethod(), request.getRequestURI(), request.getProtocol(),
                formatHeaders(request));
        }
        return true;
    }

    private String formatHeaders(HttpServletRequest request) {
        var headerNames = request.getHeaderNames();
        var sb = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            var headerName = headerNames.nextElement();
            sb.append(headerName);
            sb.append(": ");
            sb.append(request.getHeader(headerName));
            sb.append("\n");
        }
        return sb.toString();
    }

}
