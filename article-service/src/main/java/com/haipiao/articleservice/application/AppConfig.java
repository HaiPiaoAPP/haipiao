package com.haipiao.articleservice.application;

import com.haipiao.common.config.CommonConfig;
import com.haipiao.common.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(CommonConfig.class)
@ComponentScan(basePackages = {"com.haipiao.articleservice.handler", "com.haipiao.articleservice.service"})
public class AppConfig implements WebMvcConfigurer {

    @Autowired private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

}
