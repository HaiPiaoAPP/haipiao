package com.haipiao.persist.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "comment_reply")
public class CommentReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private int replyId;

    @Column(name = "article_id")
    private int articleId;

    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "text_body")
    private String textBody;

    private int likes;

    @Column(name = "replier_id")
    private int replierId;

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReplierId() {
        return replierId;
    }

    public void setReplierId(int replierId) {
        this.replierId = replierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentReply that = (CommentReply) o;
        return replyId == that.replyId &&
            articleId == that.articleId &&
            commentId == that.commentId &&
            likes == that.likes &&
            replierId == that.replierId &&
            Objects.equals(textBody, that.textBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyId, articleId, commentId, textBody, likes, replierId);
    }

    @Override
    public String toString() {
        return "CommentReply{" +
            "replyId=" + replyId +
            ", articleId=" + articleId +
            ", commentId=" + commentId +
            ", textBody='" + textBody + '\'' +
            ", likes=" + likes +
            ", replierId=" + replierId +
            '}';
    }
}
