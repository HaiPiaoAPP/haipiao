package com.haipiao.userservice.handler;

import com.google.gson.Gson;
import com.haipiao.common.config.CommonConfig;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.userservice.req.CreateUserRequest;
import com.haipiao.userservice.req.GetUserRequest;
import com.haipiao.userservice.resp.CreateUserResponse;
import com.haipiao.userservice.resp.GetUserResponse;
import org.assertj.core.util.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = {UserHandlerTest.Config.class, CommonConfig.class})
public class UserHandlerTest {

    @Configuration
    static class Config {
        @Bean
        public CreateUserHandler createUserHandler(@Autowired UserRepository userRepository,
                                                   @Autowired SessionService sessionService,
                                                   @Autowired Gson gson,
                                                   @Autowired RedisClientWrapper redisClient) {
            return new CreateUserHandler(sessionService, gson, redisClient, userRepository);
        }

        @Bean
        public GetUserHandler getUserHandler(@Autowired UserRepository userRepository,
                                             @Autowired SessionService sessionService) {
            return new GetUserHandler(sessionService, userRepository);
        }
    }

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private GetUserHandler getUserHandler;

    @Before
    public void setUp() {
        Preconditions.checkNotNull(createUserHandler);
        Preconditions.checkNotNull(getUserHandler);
    }

    @Test
    public void testCreateUserHandler() {
        CreateUserRequest createReq = new CreateUserRequest();
        createReq.setName("Alice");
        createReq.setBirthday("31/12/1999");
        createReq.setGender("F");
        ResponseEntity<CreateUserResponse> createResp = createUserHandler.handle(createReq);
        assertTrue(createResp.getBody().getStatusCode() == StatusCode.SUCCESS);
        assertNotNull(createResp.getBody().getData().getId());

        int id = createResp.getBody().getData().getId();
        GetUserRequest getReq = new GetUserRequest();
        getReq.setId(id);
        ResponseEntity<GetUserResponse> getResp = getUserHandler.handle(getReq);
        assertTrue(createResp.getBody().getStatusCode() == StatusCode.SUCCESS);
        assertEquals(getResp.getBody().getData().getName(), "Alice");
    }
}
