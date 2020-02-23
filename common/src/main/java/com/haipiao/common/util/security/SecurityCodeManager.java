package com.haipiao.common.util.security;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.haipiao.common.enums.SecurityCodeType;
import com.haipiao.common.redis.RedisClientWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Optional;

public class SecurityCodeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityCodeManager.class);

    private final Gson gson;
    private final RedisClientWrapper redisClient;
    private final Map<SecurityCodeType, SecurityCodeConfig> configMap;

    public SecurityCodeManager(@Autowired Gson gson,
                               RedisClientWrapper redisClient,
                               Map<SecurityCodeType, SecurityCodeConfig> configMap) {
        this.gson = gson;
        this.redisClient = redisClient;
        this.configMap = configMap;
    }

    @Nullable
    public String getSecurityCode(String cell, SecurityCodeType securityCodeType) {
        Preconditions.checkNotNull(securityCodeType);
        SecurityCodeConfig config = Preconditions.checkNotNull(configMap.get(securityCodeType));
        String code = RandomStringUtils.randomNumeric(config.getLength());
        String cacheKey = constructCacheKey(cell, securityCodeType);
        String cacheVal = redisClient.get(cacheKey);
        long currTimeMills = System.currentTimeMillis();
        if (cacheVal != null) {
            SecurityCodeInfo scInfo = gson.fromJson(redisClient.get(cacheKey), SecurityCodeInfo.class);
            if (currTimeMills - scInfo.getVendedTimeEpoch() < config.getMinWait()) {
                LOGGER.debug("Not sending new security code. lastIssuedTime={}, currentTime={}",
                    scInfo.getVendedTimeEpoch(), currTimeMills);
                return null;
            }
        }
        redisClient.set(cacheKey, gson.toJson(new SecurityCodeInfo(code, currTimeMills)), config.getTtl());
        return code;
    }

    public boolean validateSecurityCode(String cell, String securityCode, SecurityCodeType securityCodeType) {
        String cacheKey = constructCacheKey(cell, securityCodeType);
        boolean validated = Optional.ofNullable(redisClient.get(cacheKey))
            .map(val -> gson.fromJson(val, SecurityCodeInfo.class))
            .map(sc -> sc.getSecurityCode().equals(securityCode))
            .orElse(false);
        if (validated) {
            redisClient.delete(cacheKey);
        }
        return validated;
    }

    private String constructCacheKey(String cell, SecurityCodeType securityCodeType) {
        return cell + "_" + securityCodeType.name();
    }

    public class SecurityCodeInfo {

        private final String securityCode;
        private final long vendedTimeEpoch;

        SecurityCodeInfo(String securityCode, long vendedTimeEpoch) {
            this.securityCode = securityCode;
            this.vendedTimeEpoch = vendedTimeEpoch;
        }

        String getSecurityCode() {
            return securityCode;
        }

        long getVendedTimeEpoch() {
            return vendedTimeEpoch;
        }

    }

}
