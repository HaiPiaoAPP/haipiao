package com.haipiao.persist.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author wangjipeng
 */
@Entity
@Table(name = "article_like_relation")
public class ArticleLikeRelation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "article_id")
    private int articleId;

    @Column(name = "liker_follower_count_approximate")
    private int likerFollowerCountApproximate;

    public ArticleLikeRelation() {
    }

    public ArticleLikeRelation(int userId, int articleId, int likerFollowerCountApproximate) {
        this.userId = userId;
        this.articleId = articleId;
        this.likerFollowerCountApproximate = likerFollowerCountApproximate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getLikerFollowerCountApproximate() {
        return likerFollowerCountApproximate;
    }

    public void setLikerFollowerCountApproximate(int likerFollowerCountApproximate) {
        this.likerFollowerCountApproximate = likerFollowerCountApproximate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleLikeRelation)) return false;
        ArticleLikeRelation that = (ArticleLikeRelation) o;
        return getUserId() == that.getUserId() &&
                getArticleId() == that.getArticleId() &&
                getLikerFollowerCountApproximate() == that.getLikerFollowerCountApproximate() &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getArticleId(), getLikerFollowerCountApproximate());
    }

    @Override
    public String toString() {
        return "ArticleLikeRelation{" +
                "id=" + id +
                ", userId=" + userId +
                ", articleId=" + articleId +
                ", likerFollowerCountApproximate=" + likerFollowerCountApproximate +
                '}';
    }
}
