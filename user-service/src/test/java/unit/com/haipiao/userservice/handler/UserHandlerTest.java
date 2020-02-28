package com.haipiao.userservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.haipiao.common.config.CommonConfig;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.*;
import com.haipiao.persist.repository.*;
import com.haipiao.userservice.enums.GetCategoryEnum;
import com.haipiao.userservice.req.*;
import com.haipiao.userservice.resp.*;
import org.assertj.core.util.Preconditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

        // TODO start
        @Bean
        public GetCategoryHandler getCategoryHandler(@Autowired SessionService sessionService,
                                                     @Autowired CategoryRepository categoryRepository){
            return new GetCategoryHandler(sessionService, categoryRepository);
        }

        @Bean
        public GetUserAllCategoryHandler getUserAllCategoryHandler(@Autowired SessionService sessionService,
                                                                   @Autowired UserRepository userRepository,
                                                                   @Autowired CategoryRepository categoryRepository,
                                                                   @Autowired UserCategoryRelationRepository userCategoryRelationRepository){
            return new GetUserAllCategoryHandler(sessionService, userRepository, categoryRepository, userCategoryRelationRepository);
        }

        @Bean
        public SaveCategoryHandler saveCategoryHandler(@Autowired SessionService sessionService,
                                                       @Autowired UserCategoryRelationRepository userCategoryRelationRepository){
            return new SaveCategoryHandler(sessionService, userCategoryRelationRepository);
        }

        @Bean
        public FolloweeUserHandler followeeUserHandler(@Autowired SessionService sessionService,
                                                       @Autowired UserRepository userRepository,
                                                       @Autowired UserFollowingRelationRepository userFollowingRelationRepository){
            return new FolloweeUserHandler(sessionService, userRepository, userFollowingRelationRepository);
        }
    }

    @Autowired
    private GetCategoryHandler getCategoryHandler;

    @Autowired
    private GetUserAllCategoryHandler getUserAllCategoryHandler;

    @Autowired
    private SaveCategoryHandler saveCategoryHandler;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserCategoryRelationRepository userCategoryRelationRepository;

    @Autowired
    private FolloweeUserHandler followeeUserHandler;

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

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowingRelationRepository userFollowingRelationRepository;

    @Autowired
    private ArticleDislikeRelationRepository articleDislikeRelationRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReplyRepository commentReplyRepository;

    @Autowired
    private ArticleLikeRelationRepository articleLikeRelationRepository;

    @Autowired
    private UserAlbumRelationRepository userAlbumRelationRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ArticleCollectRelationRepository articleCollectRelationRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ArticleTopicRepository articleTopicRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    public void clearing(){
        userCategoryRelationRepository.deleteAll();
        categoryRepository.deleteAll();
        userFollowingRelationRepository.deleteAll();
        userGroupRepository.deleteAll();

        articleCollectRelationRepository.deleteAll();
        articleDislikeRelationRepository.deleteAll();
        articleLikeRelationRepository.deleteAll();
        tagRepository.deleteAll();
        imageRepository.deleteAll();
        commentReplyRepository.deleteAll();
        commentRepository.deleteAll();
        userAlbumRelationRepository.deleteAll();
        albumRepository.deleteAll();

        articleTopicRepository.deleteAll();
        articleRepository.deleteAll();

        userSessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Before
    public void setUp() {
        Preconditions.checkNotNull(createUserHandler);
        Preconditions.checkNotNull(getUserHandler);
        Preconditions.checkNotNull(updateFollowingHandler);
        Preconditions.checkNotNull(getCategoryHandler);
        Preconditions.checkNotNull(getUserAllCategoryHandler);
        Preconditions.checkNotNull(saveCategoryHandler);
        Preconditions.checkNotNull(followeeUserHandler);

        clearing();
    }

    @After
    public void unitAfter(){
        clearing();
    }

    @Test
    public void testFolloweeUserHandler(){
        UserGroup userGroup = commonSource();

        User user2 = new User();
        user2.setRealName("test case follow2");
        User save2 = userRepository.save(user2);

        FolloweeUserRequest request = new FolloweeUserRequest();
        request.setLoggedInUserId(save2.getUserId());
        request.setFolloweeId(userGroup.getOwnerId());
        request.setGroupId(userGroup.getGroupId());
        ResponseEntity<FolloweeUserResponse> handle = followeeUserHandler.handle(request);
        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);

    }

    @Test
    public void testGetCategoryHandler(){
        commonCategorySource(saveCategories());

        GetCategoryRequest request = new GetCategoryRequest();
        request.setType(GetCategoryEnum.DEFAULT.getValue());
        ResponseEntity<GetCategoryResponse> handle = getCategoryHandler.handle(request);

        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
        assertSame(handle.getBody().getData().getCategories().size(), 1);
    }

    @Test
    public void testGetUserAllCategoryHandler(){
        categoryRepository.deleteAll();
        int userId = commonCategorySource(saveCategories());

        GetUserAllCategoryRequest request = new GetUserAllCategoryRequest();
        request.setId(userId);
        ResponseEntity<GetUserAllCategoryResponse> handle = getUserAllCategoryHandler.handle(request);

        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
        assertSame(handle.getBody().getData().getUser().size(), 3);
        assertSame(handle.getBody().getData().getHot().size(), 1);
        assertSame(handle.getBody().getData().getOthers().size(), 1);
    }

    @Test
    public void testSaveCategoryHandler(){
        List<Integer> list = saveCategories();
        int userId = commonCategorySource(list);

        SaveUserCategoryRequest request = new SaveUserCategoryRequest();
        request.setCategories(list);
        request.setLoggedInUserId(userId);
        ResponseEntity<SaveUserCategoryResponse> handle = saveCategoryHandler.handle(request);

        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
    }

    private int commonCategorySource(List<Integer> list){
        User user = new User();
        user.setRealName("test case for category");
        User save = userRepository.save(user);

        UserCategoryRelation relation = new UserCategoryRelation();
        relation.setCategoryId(list.get(0));
        relation.setUserId(save.getUserId());
        UserCategoryRelation relation2 = new UserCategoryRelation();
        relation2.setCategoryId(list.get(1));
        relation2.setUserId(save.getUserId());
        UserCategoryRelation relation3 = new UserCategoryRelation();
        relation3.setCategoryId(list.get(2));
        relation3.setUserId(save.getUserId());

        userCategoryRelationRepository.saveAll(Arrays.asList(relation, relation2, relation3));
        return save.getUserId();
    }

    private List<Integer> saveCategories(){
        Category category1 = new Category();
        category1.setCategoryName("test");
        category1.setType(GetCategoryEnum.DEFAULT.getValue());
        Category save1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setCategoryName("test2");
        category2.setType(GetCategoryEnum.HOT.getValue());
        Category save2 = categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setCategoryName("test3");
        category3.setType(GetCategoryEnum.MISC.getValue());
        Category save3 = categoryRepository.save(category3);

        return Arrays.asList(save1.getCategoryId(), save2.getCategoryId(), save3.getCategoryId());
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
    @Ignore
    @Test
    public void testUpdateFollowingHandler() {
        UpdateFollowingRequest updateFollowingRequest = new UpdateFollowingRequest();
        updateFollowingRequest.setType("");
        ResponseEntity<UpdateFollowingResponse> updateFollowingResponse = updateFollowingHandler.handle(updateFollowingRequest);
        assertSame(Objects.requireNonNull(updateFollowingResponse.getBody()).getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(updateFollowingResponse.getBody().getData().getUpdated());
    }

    /**
     * API-15
     * 获取当前用户所创建的所有“分组”,默认分组等
     */
    @Test
    public void testGetUserGroupHandler() {
        UserGroup userGroup = commonSource();
        GetUserGroupRequest getUserGroupRequest = new GetUserGroupRequest();
        getUserGroupRequest.setId(userGroup.getOwnerId());
        getUserGroupRequest.setType(userGroup.getType());
        ResponseEntity<GetGroupResponse> handle = getUserGroupHandler.handle(getUserGroupRequest);
        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getGroups());
    }

    private UserGroup commonSource(){
        User user = new User();
        user.setRealName("test case user1");
        User save = userRepository.save(user);

        userGroupRepository.saveByHandle(1, save.getUserId(), "ALL");

        UserGroup group = new UserGroup();
        group.setOwnerId(save.getUserId());
        group.setType("ALL");
        return userGroupRepository.save(group);
    }

    /**
     * API-17
     * 删除分组
     */
    @Test
    public void testDeleteGroupHandler() {
        UserGroup source = commonSource();
        DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest();
        deleteGroupRequest.setLoggedInUserId(source.getOwnerId());
        deleteGroupRequest.setId(source.getGroupId());

        User user = new User();
        user.setRealName("test");
        User save = userRepository.save(user);

        UserFollowingRelation relation = new UserFollowingRelation();
        relation.setGroupId(source.getGroupId());
        relation.setUserId(source.getOwnerId());
        relation.setFollowingUserId(save.getUserId());
        userFollowingRelationRepository.save(relation);

        ResponseEntity<OperateResponse> handle = deleteGroupHandler.handle(deleteGroupRequest);
        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
    }

    /**
     * API-18
     * 新建分组
     */
    @Test
    public void testCreateGroupHandler() {
        UserGroup group = commonSource();

        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setLoggedInUserId(group.getOwnerId());
        createGroupRequest.setGroupName(group.getName());
        ResponseEntity<OperateResponse> handle = createGroupHandler.handle(createGroupRequest);
        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
    }

    /**
     * API-22
     * 获取用户所有粉丝
     */
    @Test
    public void testGetUserFollowerHandler() throws JsonProcessingException {
        UserGroup userGroup = commonSource();
        User user = new User();
        user.setRealName("test case follower");
        User save = userRepository.save(user);

        UserFollowingRelation followingRelation = new UserFollowingRelation();
        followingRelation.setUserId(userGroup.getOwnerId());
        followingRelation.setFollowingUserId(save.getUserId());
        followingRelation.setGroupId(userGroup.getGroupId());
        userFollowingRelationRepository.save(followingRelation);

        GetUserFollowerRequest getUserFollowerRequest = new GetUserFollowerRequest();
        getUserFollowerRequest.setId(userGroup.getOwnerId());
        getUserFollowerRequest.setCursor("0");
        getUserFollowerRequest.setLimit(6);
        ResponseEntity<GetUserFollowerResponse> handle = getUserFollowerHandler.handle(getUserFollowerRequest);
        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getFollowers());
    }

}
