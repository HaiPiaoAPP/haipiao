package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.dto.common.Tag;
import com.haipiao.articleservice.dto.common.Topic;
import com.haipiao.articleservice.dto.req.*;
import com.haipiao.articleservice.dto.resp.*;
import com.haipiao.common.config.CommonConfig;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.*;
import com.haipiao.persist.enums.ArticleStatus;
import com.haipiao.persist.repository.*;
import org.assertj.core.util.Preconditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.DataInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = {ArticleHandlerTest.Config.class, CommonConfig.class})
public class ArticleHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(ArticleHandlerTest.class);

    @Configuration
    static class Config {

        @Bean
        public CreateArticleHandler createArticleHandler(
                @Autowired SessionService sessionService,
                @Autowired ArticleRepository articleRepository,
                @Autowired ImageRepository imageRepository,
                @Autowired TopicRepository topicRepository,
                @Autowired ArticleTopicRepository articleTopicRepository,
                @Autowired TagRepository tagRepository) {
            return new CreateArticleHandler(
                    sessionService,
                    articleRepository,
                    topicRepository,
                    articleTopicRepository,
                    imageRepository,
                    tagRepository);
        }

        @Bean
        public GetArticleHandler getArticleHandler(
                @Autowired SessionService sessionService,
                @Autowired ArticleRepository articleRepository,
                @Autowired UserRepository userRepository,
                @Autowired ImageRepository imageRepository,
                @Autowired TopicRepository topicRepository,
                @Autowired ArticleTopicRepository articleTopicRepository,
                @Autowired TagRepository tagRepository) {
            return new GetArticleHandler(
                    sessionService,
                    articleRepository,
                    userRepository,
                    topicRepository,
                    articleTopicRepository,
                    imageRepository,
                    tagRepository);
        }

        @Bean
        public GetUserArticleHandler getUserArticleHandler(
                @Autowired SessionService sessionService,
                @Autowired ArticleRepository articleRepository,
                @Autowired ImageRepository imageRepository,
                @Autowired UserRepository userRepository,
                @Autowired ArticleLikeRelationRepository articleLikeRelationRepository) {
            return new GetUserArticleHandler(
                    sessionService,
                    articleRepository,
                    imageRepository,
                    userRepository,
                    articleLikeRelationRepository);
        }

        @Bean
        public GetUserCollectionHandler getUserCollectionHandler(
                @Autowired SessionService sessionService,
                @Autowired ArticleRepository articleRepository,
                @Autowired ImageRepository imageRepository,
                @Autowired UserRepository userRepository,
                @Autowired ArticleLikeRelationRepository articleLikeRelationRepository) {
            return new GetUserCollectionHandler(
                    sessionService,
                    articleRepository,
                    imageRepository,
                    userRepository,
                    articleLikeRelationRepository);
        }

        @Bean
        public GetUserAlbumHandler getUserAlbumHandler(
                @Autowired SessionService sessionService,
                @Autowired AlbumRepository albumRepository,
                @Autowired ImageRepository imageRepository,
                @Autowired ArticleRepository articleRepository,
                @Autowired UserAlbumRelationRepository userAlbumRelationRepository) {
            return new GetUserAlbumHandler(
                    sessionService,
                    albumRepository,
                    imageRepository,
                    articleRepository,
                    userAlbumRelationRepository);
        }

        @Bean
        public GetUserFollowerHandler getUserFollowerHandler(
                @Autowired SessionService sessionService,
                @Autowired UserRepository userRepository,
                @Autowired UserFollowingRelationRepository userFollowingRelationRepository) {
            return new GetUserFollowerHandler(
                    sessionService,
                    userRepository,
                    userFollowingRelationRepository);
        }

        @Bean
        public DeleteAndLikeArticleHandler deleteAndLikeArticleHandler(
                @Autowired SessionService sessionService,
                @Autowired UserFollowingRelationRepository userFollowingRelationRepository,
                @Autowired ArticleRepository articleRepository,
                @Autowired ArticleLikeRelationRepository articleLikeRelationRepository) {
            return new DeleteAndLikeArticleHandler(
                    sessionService,
                    userFollowingRelationRepository,
                    articleRepository,
                    articleLikeRelationRepository);
        }

        @Bean
        public DisLikeArticleHandler disLikeArticleHandler(
                @Autowired SessionService sessionService,
                @Autowired ArticleDislikeRelationRepository articleDislikeRelationRepository) {
            return new DisLikeArticleHandler(
                    sessionService,
                    articleDislikeRelationRepository);
        }

        @Bean
        public GetArticleCommentsHandler getArticleCommentsHandler(
                @Autowired SessionService sessionService,
                @Autowired ArticleRepository articleRepository,
                @Autowired UserRepository userRepository,
                @Autowired CommentRepository commentRepository,
                @Autowired CommentReplyRepository commentReplyRepository) {
            return new GetArticleCommentsHandler(
                    sessionService,
                    commentRepository,
                    commentReplyRepository,
                    articleRepository,
                    userRepository);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateArticleHandler createArticleHandler;

    @Autowired
    private GetArticleHandler getArticleHandler;

    @Autowired
    private GetUserArticleHandler getUserArticleHandler;

    @Autowired
    private GetUserCollectionHandler getUserCollectionHandler;

    @Autowired
    private GetUserAlbumHandler getUserAlbumHandler;

    @Autowired
    private GetUserFollowerHandler getUserFollowerHandler;

    @Autowired
    private DeleteAndLikeArticleHandler deleteAndLikeArticleHandler;

    @Autowired
    private DisLikeArticleHandler disLikeArticleHandler;

    @Autowired
    private GetArticleCommentsHandler getArticleCommentsHandler;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleDislikeRelationRepository articleDislikeRelationRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReplyRepository commentReplyRepository;

    @Autowired
    private UserFollowingRelationRepository userFollowingRelationRepository;

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

    private int userId;

    @Before
    public void setUp() {
        Preconditions.checkNotNull(createArticleHandler);
        Preconditions.checkNotNull(getArticleHandler);

//        clearing();
    }

    @After
    public void unitAfter(){
//        clearing();
    }

    private void clearing(){
        userRepository.deleteAll();
        userFollowingRelationRepository.deleteAll();
        userAlbumRelationRepository.deleteAll();
        articleLikeRelationRepository.deleteAll();
        albumRepository.deleteAll();
        articleDislikeRelationRepository.deleteAll();
        articleCollectRelationRepository.deleteAll();
        commentReplyRepository.deleteAll();
        commentRepository.deleteAll();
        imageRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @Test
    public void testCreateGetArticleHandler() {
        User user = new User();
        user.setUserName("blah");
        user = userRepository.save(user);
        this.userId = user.getUserId();

        CreateArticleRequest createReq = new CreateArticleRequest();
        createReq.setLoggedInUserId(userId);
        String title = "This is a Title";
        createReq.setTitle(title);
        String textBody = "This text body.";
        createReq.setText(textBody);
        Topic[] topics = new Topic[3];
        for (int i = 0; i < topics.length; i++) {
            topics[i] = new Topic();
            topics[i].setId(i + 10);
            topics[i].setName("this is topic" + i);
        }
        createReq.setTopics(topics);
        CreateArticleRequest.Image[] images = new CreateArticleRequest.Image[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = new CreateArticleRequest.Image();
            images[i].setExternalUrl("https://foo.com/bar" + i);
            images[i].setHashDigest(("hashdigest" + i).getBytes());
            Tag[] tags = new Tag[3];
            for (int j = 0; j < tags.length; j++) {
                tags[j] = new Tag();
                tags[j].setText("this is tag" + j);
                tags[j].setX(40 + j);
                tags[j].setY(50 + j);
            }
            images[i].setTags(tags);
        }
        createReq.setImages(images);

        ResponseEntity<CreateArticleResponse> createResp = createArticleHandler.handle(createReq);
        assertSame(createResp.getBody().getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(createResp.getBody().getData().getId());

        int id = createResp.getBody().getData().getId();
        GetArticleRequest getReq = new GetArticleRequest();
        getReq.setId(id);
        ResponseEntity<GetArticleResponse> getResp = getArticleHandler.handle(getReq);
        assertSame(createResp.getBody().getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(getResp.getBody().getData().getAuthor());
        GetArticleResponse.ArticleData.Image[] actualImages = getResp.getBody().getData().getImages();
        assertEquals(images.length, actualImages.length);
        for (int i = 0; i < images.length; i++) {
            assertTrue(actualImages[i].getUrl().startsWith(images[i].getExternalUrl()));
            Tag[] actualTags = actualImages[i].getTags();
            for (int j = 0; j < actualTags.length; j++) {
                assertEquals(actualTags[j].getText(), "this is tag" + j);
                assertEquals(actualTags[j].getX(), 40 + j);
                assertEquals(actualTags[j].getY(), 50 + j);
            }
        }
        Topic[] actualTopics = getResp.getBody().getData().getTopics();
        assertEquals(topics.length, actualTopics.length);
        assertEquals(Arrays.stream(actualTopics).map(Topic::getName).collect(toSet()), Arrays.stream(topics).map(Topic::getName).collect(toSet()));
        assertEquals(title, getResp.getBody().getData().getTitle());
        assertEquals(textBody, getResp.getBody().getData().getText());
        assertEquals(0, getResp.getBody().getData().getShares());
        assertEquals(0, getResp.getBody().getData().getLikes());
        assertEquals(0, getResp.getBody().getData().getCollects());
    }

    /**
     * API-19
     * 获取用户笔记
     * <p>
     * API-20
     * 获取用户收藏笔记
     */
    @Test
    public void testGetUserArticleAndCollectionHandler() {
        List<Integer> list = commonUserSource();
        Image image = new Image();
        image.setArticleId(list.get(1));
        image.setPositionIdx(0);
        imageRepository.save(image);

        ArticleCollectRelation relation = new ArticleCollectRelation();
        relation.setArticleId(list.get(1));
        relation.setCollectorId(list.get(0));
        relation.setCreateTs(new Date());
        articleCollectRelationRepository.save(relation);


        GetArticleCommentsRequest getArticleCommentsRequest = new GetArticleCommentsRequest();
        getArticleCommentsRequest.setId(list.get(0));
        getArticleCommentsRequest.setCursor("0");
        getArticleCommentsRequest.setLimit(6);

        //获取用户笔记
        ResponseEntity<ArticleResponse> handle = getUserArticleHandler.handle(getArticleCommentsRequest);
        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getArticles());

        //获取用户收藏笔记
        ResponseEntity<ArticleResponse> getUserCollectionHandle = getUserCollectionHandler.handle(getArticleCommentsRequest);
        assertSame(Objects.requireNonNull(getUserCollectionHandle.getBody()).getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(getUserCollectionHandle.getBody().getData().getArticles());
    }

    /**
     * API-21
     * 获取用户收藏的专辑
     */
    @Test
    public void testGetUserAlbumHandler() {
        int id = commonAlbumSource();

        GetArticleCommentsRequest getArticleCommentsRequest = new GetArticleCommentsRequest();
        getArticleCommentsRequest.setId(id);
        getArticleCommentsRequest.setCursor("0");
        getArticleCommentsRequest.setLimit(6);
        ResponseEntity<AlbumResponse> handle = getUserAlbumHandler.handle(getArticleCommentsRequest);
        assertSame(handle.getBody().getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getAlbums());
    }

    private int commonAlbumSource(){
        List<Integer> list = commonUserSource();
        Album album = new Album();
        album.setUserId(list.get(0));
        album.setAlbumName("test case album");
        Album save = albumRepository.save(album);

        UserAlbumRelation relation = new UserAlbumRelation();
        relation.setFollowerId(list.get(0));
        relation.setAlbumId(save.getAlbumId());
        relation.setCreateTs(new Date());
        userAlbumRelationRepository.save(relation);

        return list.get(0);
    }

    /**
     * API-22
     * 获取用户粉丝列表
     */
    @Test
    public void testGetUserFollowerHandler() {
        int id = commonAlbumSource();

        GetArticleCommentsRequest getArticleCommentsRequest = new GetArticleCommentsRequest();
        getArticleCommentsRequest.setId(id);
        getArticleCommentsRequest.setCursor("0");
        getArticleCommentsRequest.setLimit(6);
        ResponseEntity<FollowerResponse> handle = getUserFollowerHandler.handle(getArticleCommentsRequest);
        assertSame(handle.getBody().getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getFollowers());
    }



    /**
     * API-12
     * 用户取消文章点赞
     */
    @Test
    public void testDeleteAndLikeArticleHandler() {
        List<Integer> list = commonUserSource();

        LikeArticleRequest likeArticleRequest = new LikeArticleRequest();
        likeArticleRequest.setId(list.get(1));
        likeArticleRequest.setType(1);
        likeArticleRequest.setLoggedInUserId(list.get(0));
        ResponseEntity<LikeArticleResponse> handle = deleteAndLikeArticleHandler.handle(likeArticleRequest);
        assertSame(handle.getBody().getStatusCode(), StatusCode.SUCCESS);
    }

    private List<Integer> commonUserSource(){
        User user1 = new User();
        user1.setRealName("test case article1");
        User save1 = userRepository.save(user1);

        User user2 = new User();
        user2.setRealName("test case article2");
        user2.setUserName("test case name");
        user2.setProfileImgUrl("test case url");
        User save2 = userRepository.save(user2);

        User user3 = new User();
        user3.setRealName("test case article3");
        User save6 = userRepository.save(user3);

        Article article = new Article();
        article.setAuthorId(save1.getUserId());
        article.setLikes(1000);
        article.setStatus(ArticleStatus.PUBLISHED.getCode());
        article.setTitle("test case title");
        Article save = articleRepository.save(article);

        Comment comment = new Comment();
        comment.setArticleId(save.getArticleId());
        comment.setAuthorId(save2.getUserId());
        comment.setCreateTs(new Date());
        comment.setLikes(10);
        comment.setTextBody("test case body");
        Comment save4 = commentRepository.save(comment);

        CommentReply reply = new CommentReply();
        reply.setCommentId(save4.getCommentId());
        reply.setArticleId(save.getArticleId());
        reply.setReplierId(save6.getUserId());
        reply.setTextBody("Test Case Reply");
        reply.setCreateTs(new Date());
        commentReplyRepository.save(reply);

        ArticleLikeRelation articleLikeRelation = new ArticleLikeRelation();
        articleLikeRelation.setArticleId(article.getArticleId());
        articleLikeRelation.setUserId(save1.getUserId());
        articleLikeRelationRepository.save(articleLikeRelation);

        UserFollowingRelation relation = new UserFollowingRelation();
        relation.setFollowingUserId(save2.getUserId());
        relation.setUserId(save1.getUserId());
        relation.setGroupId(1);
        userFollowingRelationRepository.save(relation);

        return Arrays.asList(save1.getUserId(), save.getArticleId());
    }

    /**
     * API-13
     * 用户对文章不感兴趣
     */
    @Test
    public void testDisLikeArticleHandler() {
        DisLikeArticleRequest disLikeArticleRequest = new DisLikeArticleRequest();
        disLikeArticleRequest.setId(1);
        disLikeArticleRequest.setLoggedInUserId(1);
        ResponseEntity<DisLikeArticleResponse> handle = disLikeArticleHandler.handle(disLikeArticleRequest);
        assertSame(Objects.requireNonNull(handle.getBody()).getStatusCode(), StatusCode.SUCCESS);
    }

    /**
     * API-24
     * 加载评论列表
     */
    @Test
    public void testGetArticleCommentsHandler() {
        List<Integer> list = commonUserSource();

        GetArticleCommentsRequest getArticleCommentsRequest = new GetArticleCommentsRequest();
        getArticleCommentsRequest.setId(list.get(1));
        getArticleCommentsRequest.setCursor("0");
        getArticleCommentsRequest.setLimit(6);
        ResponseEntity<GetArticleCommentsResponse> handle = getArticleCommentsHandler.handle(getArticleCommentsRequest);
        assertSame(handle.getBody().getStatusCode(), StatusCode.SUCCESS);
        assertNotNull(handle.getBody().getData().getComments());
    }
}
