package com.haipiao.persist.entity;

import com.haipiao.persist.enums.ArticleStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private int articleId;

    private String title;

    @Column(name = "text_body")
    private String textBody;

    // 1: images, 2: video
    private int type; // FIX ME, use enum type

    private int likes;

    private int collects;

    private int shares;

    @Column(name = "user_id")
    private int authorId;

    @Column(name = "status")
    private ArticleStatus status;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCollects() {
        return collects;
    }

    public void setCollects(int collects) {
        this.collects = collects;
    }

    public int getShares() { return shares; }

    public void setShares(int shares) { this.shares = shares; }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return articleId == article.articleId &&
                type == article.type &&
                likes == article.likes &&
                collects == article.collects &&
                shares == article.shares &&
                authorId == article.authorId &&
                Objects.equals(title, article.title) &&
                Objects.equals(textBody, article.textBody) &&
                status == article.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, title, textBody, type, likes, collects, shares, authorId, status);
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", textBody='" + textBody + '\'' +
                ", type=" + type +
                ", likes=" + likes +
                ", collects=" + collects +
                ", shares=" + shares +
                ", authorId=" + authorId +
                ", status=" + status +
                '}';
    }
}
