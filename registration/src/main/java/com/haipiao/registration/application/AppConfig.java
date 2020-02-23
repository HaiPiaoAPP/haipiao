package com.haipiao.registration.application;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.haipiao.common.config.CommonConfig;
import com.haipiao.common.enums.SecurityCodeType;
import com.haipiao.common.interceptor.LoggingInterceptor;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.util.security.SecurityCodeConfig;
import com.haipiao.common.util.security.SecurityCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(CommonConfig.class)
@ComponentScan(basePackages = {"com.haipiao.registration.property", "com.haipiao.registration.handler"})
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

    @Bean
    public SecurityCodeManager securityCodeManager(Gson gson,
                                                   RedisClientWrapper redisClient) {
        return new SecurityCodeManager(gson, redisClient,
            ImmutableMap.of(
                SecurityCodeType.CHANGE_PASSWORD, new SecurityCodeConfig(6, 6 * 60, 5 * 1000),
                SecurityCodeType.LOGIN, new SecurityCodeConfig(6, 6 * 60, 5 * 1000)
            ));
    }

}


