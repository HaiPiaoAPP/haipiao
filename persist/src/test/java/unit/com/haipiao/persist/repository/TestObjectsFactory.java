package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.Comment;
import com.haipiao.persist.entity.CommentReply;
import com.haipiao.persist.entity.Image;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.enums.Gender;
import com.haipiao.persist.enums.ImageStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TestObjectsFactory {

    private final Random random = new Random();
    private final MessageDigest digest;

    public TestObjectsFactory() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public User createDefaultUser() {
        User user = new User();
        user.setUserName("海漂" + random.nextInt());
        user.setRealName("haiopiao");
        user.setBirthday(getCurrentDateNoTime());
        user.setGender(Gender.FEMALE);
        return user;
    }

    public Article createArticle(int authorId) {
        Article article = new Article();
        article.setAuthorId(authorId);
        article.setCollects(1000);
        article.setLikes(1000);
        article.setTitle("第一篇文章");
        article.setTextBody("This is my first #文章.");
        return article;
    }

    public Image createImage(int articleId) {
        Image image = new Image();
        image.setPositionIdx(0);
        image.setArticleId(articleId);
        image.setExternalUrl("s3://haipiao/7326");
        image.setHashDigest(digest.digest("image".getBytes()));
        image.setStatus(ImageStatus.PUBLISHED);
        return image;
    }

    public Comment createComment(int articleId, int commenterId) {
        Comment comment = new Comment();
        comment.setTextBody("This is a comment");
        comment.setLikes(1000);
        comment.setArticleId(articleId);
        comment.setAuthorId(commenterId);
        return comment;
    }

    public CommentReply createCommentReply(int articleId, int commentId, int replierId) {
        CommentReply commentReply = new CommentReply();
        commentReply.setReplierId(replierId);
        commentReply.setArticleId(articleId);
        commentReply.setCommentId(commentId);
        commentReply.setTextBody("This is a reply");
        commentReply.setLikes(1000);
        return commentReply;
    }

    private static Date getCurrentDateNoTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
