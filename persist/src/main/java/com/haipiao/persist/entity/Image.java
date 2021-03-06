package com.haipiao.persist.entity;

import com.haipiao.persist.enums.ImageStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private int imageId;

    @Column(name = "position_index")
    private int positionIdx;

    @Column(name = "article_id")
    private int articleId;

    @Column(name = "external_url")
    private String externalUrl;

    @Column(name = "external_url_large")
    private String externalUrlLarge;

    @Column(name = "external_url_medium")
    private String externalUrlMedium;

    @Column(name = "external_url_small")
    private String externalUrlSmall;

    @Column(name = "hash_digest")
    private byte[] hashDigest;

    @Column(name = "status")
    private ImageStatus status;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getPositionIdx() {
        return positionIdx;
    }

    public void setPositionIdx(int positionIdx) {
        this.positionIdx = positionIdx;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getExternalUrlLarge() {
        return externalUrlLarge;
    }

    public void setExternalUrlLarge(String externalUrlLarge) {
        this.externalUrlLarge = externalUrlLarge;
    }

    public String getExternalUrlMedium() {
        return externalUrlMedium;
    }

    public void setExternalUrlMedium(String externalUrlMedium) {
        this.externalUrlMedium = externalUrlMedium;
    }

    public String getExternalUrlSmall() {
        return externalUrlSmall;
    }

    public void setExternalUrlSmall(String externalUrlSmall) {
        this.externalUrlSmall = externalUrlSmall;
    }

    public byte[] getHashDigest() {
        return hashDigest;
    }

    public void setHashDigest(byte[] hashDigest) {
        this.hashDigest = hashDigest;
    }

    public ImageStatus getStatus() {
        return status;
    }

    public void setStatus(ImageStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return imageId == image.imageId &&
                positionIdx == image.positionIdx &&
                articleId == image.articleId &&
                Objects.equals(externalUrl, image.externalUrl) &&
                Objects.equals(externalUrlLarge, image.externalUrlLarge) &&
                Objects.equals(externalUrlMedium, image.externalUrlMedium) &&
                Objects.equals(externalUrlSmall, image.externalUrlSmall) &&
                Arrays.equals(hashDigest, image.hashDigest) &&
                status == image.status;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(imageId, positionIdx, articleId, externalUrl, externalUrlLarge, externalUrlMedium, externalUrlSmall, status);
        result = 31 * result + Arrays.hashCode(hashDigest);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", positionIdx=" + positionIdx +
                ", articleId=" + articleId +
                ", externalUrl='" + externalUrl + '\'' +
                ", externalUrlLarge='" + externalUrlLarge + '\'' +
                ", externalUrlMedium='" + externalUrlMedium + '\'' +
                ", externalUrlSmall='" + externalUrlSmall + '\'' +
                ", hashDigest=" + Arrays.toString(hashDigest) +
                ", status=" + status +
                '}';
    }
}
