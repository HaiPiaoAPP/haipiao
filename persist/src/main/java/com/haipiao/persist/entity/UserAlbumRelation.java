package com.haipiao.persist.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_album_relation")
public class UserAlbumRelation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "follower_id")
    private int followerId;

    @Column(name = "album_id")
    private int albumId;

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

    public UserAlbumRelation() {
    }

    public UserAlbumRelation(int followerId, int albumId) {
        this.followerId = followerId;
        this.albumId = albumId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAlbumRelation that = (UserAlbumRelation) o;
        return followerId == that.followerId &&
                albumId == that.albumId &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, followerId, albumId);
    }

    @Override
    public String toString() {
        return "UserAlbumRelation{" +
                "id=" + id +
                ", followerId=" + followerId +
                ", albumId=" + albumId +
                '}';
    }
}
