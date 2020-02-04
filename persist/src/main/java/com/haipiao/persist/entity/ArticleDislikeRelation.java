package com.haipiao.persist.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author wangjipeng
 */
@Entity
@Table(name = "article_dislike_relation")
public class ArticleDislikeRelation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "article_id")
    private int articleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleDislikeRelation)) return false;
        ArticleDislikeRelation that = (ArticleDislikeRelation) o;
        return id == that.id &&
                userId == that.userId &&
                articleId == that.articleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, articleId);
    }

    @Override
    public String toString() {
        return "ArticleDislikeRelation{" +
                "id=" + id +
                ", userId=" + userId +
                ", articleId=" + articleId +
                '}';
    }
}
