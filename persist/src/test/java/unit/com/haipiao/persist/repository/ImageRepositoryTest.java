package com.haipiao.persist.repository;

import com.haipiao.persist.config.PersistConfig;
import com.haipiao.persist.config.TestConfig;
import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.Image;
import com.haipiao.persist.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PersistConfig.class, TestConfig.class})
public class ImageRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    TestObjectsFactory testObjectsFactory;

    private int articleId;

    @Before
    public void setUp() {
        User author = testObjectsFactory.createDefaultUser();
        int authorId = userRepository.save(author).getUserId();
        Article article = testObjectsFactory.createArticle(authorId);
        articleId = articleRepository.save(article).getArticleId();
    }

    @Test
    public void testSaveAndRead() {
        Image image = testObjectsFactory.createImage(articleId);
        Image expected = imageRepository.save(image);
        Optional<Image> actual = imageRepository.findById(expected.getImageId());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
