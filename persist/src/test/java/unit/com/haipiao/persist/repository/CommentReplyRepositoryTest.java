package com.haipiao.persist.repository;

import com.haipiao.persist.config.PersistConfig;
import com.haipiao.persist.config.TestConfig;
import com.haipiao.persist.entity.CommentReply;
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
public class CommentReplyRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentReplyRepository commentReplyRepository;

    @Autowired
    TestObjectsFactory testObjectsFactory;

    private int userId;
    private int articleId;
    private int commentId;

    @Before
    public void setUp() {
        userId = userRepository.save(testObjectsFactory.createDefaultUser()).getUserId();
        articleId = articleRepository.save(testObjectsFactory.createArticle(userId)).getArticleId();
        commentId = commentRepository.save(testObjectsFactory.createComment(articleId, userId)).getCommentId();
    }

    @Test
    public void testSaveAndRead() {
        CommentReply commentReply = testObjectsFactory.createCommentReply(articleId, commentId, userId);
        CommentReply expected = commentReplyRepository.save(commentReply);
        Optional<CommentReply> actual = commentReplyRepository.findById(expected.getReplyId());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
