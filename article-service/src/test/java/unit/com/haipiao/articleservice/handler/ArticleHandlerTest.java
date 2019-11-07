package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.dto.common.Tag;
import com.haipiao.articleservice.dto.common.Topic;
import com.haipiao.articleservice.dto.req.CreateArticleRequest;
import com.haipiao.articleservice.dto.req.GetArticleRequest;
import com.haipiao.articleservice.dto.resp.CreateArticleResponse;
import com.haipiao.articleservice.dto.resp.GetArticleResponse;
import com.haipiao.common.config.CommonConfig;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.ArticleRepository;
import com.haipiao.persist.repository.ArticleTopicRepository;
import com.haipiao.persist.repository.ImageRepository;
import com.haipiao.persist.repository.TagRepository;
import com.haipiao.persist.repository.TopicRepository;
import com.haipiao.persist.repository.UserRepository;
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

import java.util.Arrays;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = {ArticleHandlerTest.Config.class, CommonConfig.class})
public class ArticleHandlerTest {

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
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateArticleHandler createArticleHandler;

    @Autowired
    private GetArticleHandler getArticleHandler;

    private int userId;

    @Before
    public void setUp() {
        Preconditions.checkNotNull(createArticleHandler);
        Preconditions.checkNotNull(getArticleHandler);

        User user = new User();
        user.setUserName("blah");
        user = userRepository.save(user);
        this.userId = user.getUserId();
    }

    @Test
    public void testCreateGetArticleHandler() {
        CreateArticleRequest createReq = new CreateArticleRequest();
        createReq.setAuthorId(userId);
        String title = "This is a Title";
        createReq.setTitile(title);
        String textBody = "This text body.";
        createReq.setText(textBody);
        Topic[] topics = new Topic[3];
        for (int i=0; i<topics.length; i++) {
            topics[i] = new Topic();
            topics[i].setName("this is topic"+i);
        }
        createReq.setTopics(topics);
        CreateArticleRequest.Image[] images = new CreateArticleRequest.Image[3];
        for (int i=0; i<images.length; i++) {
            images[i] = new CreateArticleRequest.Image();
            images[i].setExternalUrl("https://foo.com/bar"+i);
            images[i].setHashDigest(("hashdigest"+i).getBytes());
            Tag[] tags = new Tag[3];
            for (int j=0; j<tags.length; j++) {
                tags[j] = new Tag();
                tags[j].setText("this is tag"+j);
                tags[j].setX(40+j);
                tags[j].setY(50+j);
            }
            images[i].setTags(tags);
        }
        createReq.setImages(images);

        ResponseEntity<CreateArticleResponse> createResp = createArticleHandler.handle(createReq);
        assertTrue(createResp.getBody().getStatusCode() == StatusCode.SUCCESS);
        assertNotNull(createResp.getBody().getData().getId());

        int id = createResp.getBody().getData().getId();
        GetArticleRequest getReq = new GetArticleRequest();
        getReq.setId(id);
        ResponseEntity<GetArticleResponse> getResp = getArticleHandler.handle(getReq);
        assertTrue(createResp.getBody().getStatusCode() == StatusCode.SUCCESS);
        assertNotNull(getResp.getBody().getData().getAuthor());
        GetArticleResponse.ArticleData.Image[] actualImages = getResp.getBody().getData().getImages();
        assertEquals(images.length, actualImages.length);
        for (int i = 0; i < images.length; i++) {
            assertTrue(actualImages[i].getUrl().startsWith(images[i].getExternalUrl()));
            Tag[] actualTags = actualImages[i].getTags();
            for (int j=0; j<actualTags.length; j++) {
                assertEquals(actualTags[j].getText(), "this is tag"+j);
                assertEquals(actualTags[j].getX(), 40+j);
                assertEquals(actualTags[j].getY(), 50+j);
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
}
