package com.haipiao.persist.entity;

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

    private int likes;

    private int collects;

    @Column(name = "user_id")
    private int authorId;

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
        Article article = (Article) o;
        return articleId == article.articleId &&
            likes == article.likes &&
            collects == article.collects &&
            authorId == article.authorId &&
            Objects.equals(title, article.title) &&
            Objects.equals(textBody, article.textBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, title, textBody, likes, collects, authorId);
    }

    @Override
    public String toString() {
        return "Article{" +
            "articleId=" + articleId +
            ", title='" + title + '\'' +
            ", textBody='" + textBody + '\'' +
            ", likes=" + likes +
            ", collects=" + collects +
            ", authorId=" + authorId +
            '}';
    }
}
