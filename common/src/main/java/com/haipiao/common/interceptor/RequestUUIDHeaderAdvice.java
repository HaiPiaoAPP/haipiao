package com.haipiao.common.interceptor;

import static com.haipiao.common.constant.LoggingConstant.REQUEST_UUID;
import static com.haipiao.common.constant.LoggingConstant.X_HAIPIAO_REQUEST_UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class RequestUUIDHeaderAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        response.getHeaders().add(X_HAIPIAO_REQUEST_UUID, MDC.get(REQUEST_UUID));
        var servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        logger.info("\n-------- Response --------\n"
                + "{}\n"
                + "{}"
                + "--------------------------\n",
            servletResponse.getStatus(), formatHeaders(response));
        return body;
    }

    private String formatHeaders(ServerHttpResponse response) {
        var sb = new StringBuilder();
        for (var entry : response.getHeaders().entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

}
