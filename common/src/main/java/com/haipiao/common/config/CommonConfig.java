package com.haipiao.common.config;

import com.google.gson.Gson;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.redis.RedisConfig;
import com.haipiao.persist.config.PersistConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistConfig.class)
@ComponentScan(basePackages = {"com.haipiao.common.service", "com.haipiao.common.interceptor"})
public class CommonConfig {

    @Value("${redis.service:localhost:16379}")
    private String service;
    @Value("${redis.dbIndex:0}")
    private int dbIndex;
    @Value("${redis.defaultTTL:600}")
    private long defaultTTL;

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public RedisClientWrapper redisClient() {
        String[] parts = service.split(":");
        return new RedisClientWrapper(new RedisConfig()
            .setHostname(parts[0])
            .setPort(Integer.parseInt(parts[1]))
            .setDbIndex(dbIndex)
            .setDefaultTTL(defaultTTL));
    }

}
