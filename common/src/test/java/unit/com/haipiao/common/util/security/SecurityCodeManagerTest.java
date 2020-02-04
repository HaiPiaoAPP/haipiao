package com.haipiao.common.util.security;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.haipiao.common.enums.SecurityCodeType;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.redis.RedisConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecurityCodeManagerTest {

    private SecurityCodeManager scManager;

    @Before
    public void setUp() {
        RedisClientWrapper redisClient = new RedisClientWrapper(new RedisConfig()
            .setHostname("localhost")
            .setPort(16379)
            .setDbIndex(0)
            .setDefaultTTL(10*60));
        scManager = new SecurityCodeManager(new Gson(), redisClient, ImmutableMap.of(
            SecurityCodeType.LOGIN, new SecurityCodeConfig(6, 1, 1000),
            SecurityCodeType.CHANGE_PASSWORD, new SecurityCodeConfig(6, 1, 1000)
        ));
    }

    @Test
    public void testGetAndValidateSecurityCode() {
        String cell = "8888881";
        String sc = scManager.getSecurityCode(cell, SecurityCodeType.LOGIN);
        assertTrue(scManager.validateSecurityCode(cell, sc, SecurityCodeType.LOGIN));
    }

    @Test
    public void testWrongCellNumber() {
        String cell = "8888882";
        String sc = scManager.getSecurityCode(cell, SecurityCodeType.LOGIN);
        assertFalse(scManager.validateSecurityCode(cell + "**", sc, SecurityCodeType.LOGIN));
    }

    @Test
    public void testWrongSecurityCode() {
        String cell = "8888883";
        String sc = scManager.getSecurityCode(cell, SecurityCodeType.LOGIN);
        assertFalse(scManager.validateSecurityCode(cell, sc + "**", SecurityCodeType.LOGIN));
    }

    @Test
    public void testSecurityCodeExpires() throws Exception {
        String cell = "8888884";
        String sc = scManager.getSecurityCode(cell, SecurityCodeType.LOGIN);
        Thread.sleep(1500);
        assertFalse(scManager.validateSecurityCode(cell, sc, SecurityCodeType.LOGIN));
    }

    @Test
    public void testSuccessfulValidationShouldCleanCache() {
        String cell = "8888885";
        String sc = scManager.getSecurityCode(cell, SecurityCodeType.LOGIN);
        assertTrue(scManager.validateSecurityCode(cell, sc, SecurityCodeType.LOGIN));
        assertFalse(scManager.validateSecurityCode(cell, sc, SecurityCodeType.LOGIN));
    }

    @Test
    public void testThrottling() {
        String cell = "8888886";
        assertNotNull(scManager.getSecurityCode(cell, SecurityCodeType.LOGIN));
        assertNull(scManager.getSecurityCode(cell, SecurityCodeType.LOGIN));
    }

    @Test
    public void testThrottledAndWait() throws Exception {
        String cell = "8888887";
        assertNotNull(scManager.getSecurityCode(cell, SecurityCodeType.LOGIN));
        Thread.sleep(1500);
        assertNotNull(scManager.getSecurityCode(cell, SecurityCodeType.LOGIN));
    }

    @Test
    public void testThrottleNoWait() {
        String cell1 = "1888888";
        String cell2 = "2888888";
        assertNotNull(scManager.getSecurityCode(cell1, SecurityCodeType.LOGIN));
        assertNotNull(scManager.getSecurityCode(cell2, SecurityCodeType.LOGIN));
    }

    @Test
    public void testLastSecurityCodeWin() throws Exception {
        String cell = "1234567";
        String sc1 = scManager.getSecurityCode(cell, SecurityCodeType.LOGIN);
        Thread.sleep(1500);
        String sc2 = scManager.getSecurityCode(cell, SecurityCodeType.LOGIN);
        assertFalse(scManager.validateSecurityCode(cell, sc1, SecurityCodeType.LOGIN));
        assertTrue(scManager.validateSecurityCode(cell, sc2, SecurityCodeType.LOGIN));
    }

}
