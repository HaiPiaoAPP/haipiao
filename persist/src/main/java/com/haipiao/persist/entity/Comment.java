package com.haipiao.persist.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "text_body")
    private String textBody;

    private int likes;

    @Column(name = "article_id")
    private int articleId;

    @Column(name = "author_id")
    private int authorId;

    @Column(name = "create_ts")
    private Date createTs;

    @Column(name = "update_ts")
    private Date updateTs;

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public Date getUpdateTs() {
        return updateTs;
    }

    @Override
    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
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

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment that = (Comment) o;
        return commentId == that.commentId &&
            likes == that.likes &&
            articleId == that.articleId &&
            authorId == that.authorId &&
            Objects.equals(textBody, that.textBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, textBody, likes, articleId, authorId);
    }

    @Override
    public String toString() {
        return "CommentReply{" +
            "commentId=" + commentId +
            ", textBody='" + textBody + '\'' +
            ", likes=" + likes +
            ", articleId=" + articleId +
            ", authorId=" + authorId +
            '}';
    }

}
