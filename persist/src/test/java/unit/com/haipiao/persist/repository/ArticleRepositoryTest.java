package com.haipiao.persist.repository;

import com.haipiao.persist.config.PersistConfig;
import com.haipiao.persist.config.TestConfig;
import com.haipiao.persist.entity.Article;
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
public class ArticleRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    TestObjectsFactory testObjectsFactory;

    private int authorId;

    @Before
    public void setUp() {
        User author = new User();
        authorId = userRepository.save(author).getUserId();
    }

    @Test
    public void testSaveAndRead() {
        Article article = testObjectsFactory.createArticle(authorId);
        Article expected = articleRepository.save(article);
        Optional<Article> actual = articleRepository.findById(article.getArticleId());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
