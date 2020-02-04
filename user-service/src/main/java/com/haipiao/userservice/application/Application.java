package com.haipiao.userservice.application;

import static com.haipiao.common.constant.LoggingConstant.APP_NAME;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, JacksonAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        MDC.put(APP_NAME, "user-service");
        SpringApplication.run(Application.class, args);
    }

}
