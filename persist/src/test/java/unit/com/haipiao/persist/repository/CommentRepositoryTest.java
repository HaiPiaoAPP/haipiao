package com.haipiao.persist.repository;

import com.haipiao.persist.config.PersistConfig;
import com.haipiao.persist.config.TestConfig;
import com.haipiao.persist.entity.Comment;
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
public class CommentRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TestObjectsFactory testObjectsFactory;

    private int userId;
    private int articleId;

    @Before
    public void setUp() {
        userId = userRepository.save(testObjectsFactory.createDefaultUser()).getUserId();
        articleId = articleRepository.save(testObjectsFactory.createArticle(userId)).getArticleId();
    }

    @Test
    public void testSaveAndRead() {
        Comment comment = testObjectsFactory.createComment(articleId, userId);
        Comment expected = commentRepository.save(comment);
        Optional<Comment> actual = commentRepository.findById(expected.getCommentId());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
