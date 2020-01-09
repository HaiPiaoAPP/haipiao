package com.haipiao.userservice.handler;

import com.google.gson.Gson;
import com.haipiao.common.config.CommonConfig;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.repository.UserFollowingRelationRepository;
import com.haipiao.persist.repository.UserGroupRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.userservice.req.*;
import com.haipiao.userservice.resp.*;
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

        @Bean
        public UpdateFollowingHandler updateFollowingHandler(@Autowired SessionService sessionService) {
            return new UpdateFollowingHandler(sessionService);
        }

        @Bean
        public GetUserGroupHandler getUserGroupHandler(@Autowired SessionService sessionService,
                                                       @Autowired UserGroupRepository userGroupRepository) {
            return new GetUserGroupHandler(sessionService, userGroupRepository);
        }

        @Bean
        public DeleteGroupHandler deleteGroupHandler(@Autowired SessionService sessionService,
                                                     @Autowired UserGroupRepository userGroupRepository,
                                                     @Autowired UserRepository userRepository,
                                                     @Autowired UserFollowingRelationRepository userFollowingRelationRepository) {
            return new DeleteGroupHandler(sessionService, userGroupRepository,userRepository,userFollowingRelationRepository);
        }

        @Bean
        public CreateGroupHandler createGroupHandler(@Autowired SessionService sessionService,
                                                     @Autowired UserGroupRepository userGroupRepository,
                                                     @Autowired UserRepository userRepository,
                                                     @Autowired UserFollowingRelationRepository userFollowingRelationRepository) {
            return new CreateGroupHandler(sessionService, userGroupRepository,userRepository,userFollowingRelationRepository);
        }

        @Bean
        public GetUserFollowerHandler getUserFollowerHandler(@Autowired SessionService sessionService,
                                                     @Autowired UserRepository userRepository,
                                                     @Autowired UserFollowingRelationRepository userFollowingRelationRepository) {
            return new GetUserFollowerHandler(sessionService,userRepository,userFollowingRelationRepository);
        }
    }

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private GetUserHandler getUserHandler;

    @Autowired
    private UpdateFollowingHandler updateFollowingHandler;

    @Autowired
    private GetUserGroupHandler getUserGroupHandler;

    @Autowired
    private DeleteGroupHandler deleteGroupHandler;

    @Autowired
    private CreateGroupHandler createGroupHandler;

    @Autowired
    private GetUserFollowerHandler getUserFollowerHandler;


    @Before
    public void setUp() {
        Preconditions.checkNotNull(createUserHandler);
        Preconditions.checkNotNull(getUserHandler);
        Preconditions.checkNotNull(updateFollowingHandler);
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

    /**
     * API-14
     * api获取关注用户的更新
     */
    @Test
    public void testUpdateFollowingHandler() {
        UpdateFollowingRequest updateFollowingRequest = new UpdateFollowingRequest();
        updateFollowingRequest.setType("");
        ResponseEntity<UpdateFollowingResponse> updateFollowingResponse = updateFollowingHandler.handle(updateFollowingRequest);
        assertTrue(updateFollowingResponse.getBody().getStatusCode() == StatusCode.SUCCESS);
        assertNotNull(updateFollowingResponse.getBody().getData().getUpdated());
    }

    /**
     * API-15
     * 获取当前用户所创建的所有“分组”,默认分组等
     */
    @Test
    public void testGetUserGroupHandler() {
        GetUserGroupRequest getUserGroupRequest = new GetUserGroupRequest();
        getUserGroupRequest.setId(1);
        getUserGroupRequest.setType("ALL");
        ResponseEntity<GetGroupResponse> handle = getUserGroupHandler.handle(getUserGroupRequest);
        assertTrue(handle.getBody().getStatusCode() == StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getGroups());
    }

    /**
     * API-17
     * 删除分组
     */
    @Test
    public void testDeleteGroupHandler() {
        DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest();
        deleteGroupRequest.setId(1);
        ResponseEntity<OperateResponse> handle = deleteGroupHandler.handle(deleteGroupRequest);
        assertTrue(handle.getBody().getStatusCode() == StatusCode.SUCCESS);
    }

    /**
     * API-18
     * 新建分组
     */
    @Test
    public void testCreateGroupHandler() {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setUserId(1);
        createGroupRequest.setGroupName("分组测试1");
        ResponseEntity<OperateResponse> handle = createGroupHandler.handle(createGroupRequest);
        assertTrue(handle.getBody().getStatusCode() == StatusCode.SUCCESS);
    }

    /**
     * API-22
     * 获取用户所有粉丝
     */
    @Test
    public void testGetUserFollowerHandler() {
        GetUserFollowerRequest getUserFollowerRequest = new GetUserFollowerRequest();
        getUserFollowerRequest.setId(1);
        getUserFollowerRequest.setCursor("0");
        getUserFollowerRequest.setLimit(6);
        ResponseEntity<GetUserFollowerResponse> handle = getUserFollowerHandler.handle(getUserFollowerRequest);
        assertTrue(handle.getBody().getStatusCode() == StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getFollowers());
    }

}
