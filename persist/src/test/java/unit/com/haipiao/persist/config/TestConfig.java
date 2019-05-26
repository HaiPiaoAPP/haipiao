package com.haipiao.persist.config;

import com.haipiao.persist.repository.TestObjectsFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public TestObjectsFactory testObjectsFactory() {
        return new TestObjectsFactory();
    }

}
