package com.haipiao.persist.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "album")
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private int albumId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "album_name")
    private String albumName;

    public Album() {
    }

    public Album(int userId, String albumName) {
        this.userId = userId;
        this.albumName = albumName;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return albumId == album.albumId &&
                userId == album.userId &&
                Objects.equals(albumName, album.albumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, userId, albumName);
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", userId=" + userId +
                ", albumName='" + albumName + '\'' +
                '}';
    }
}
