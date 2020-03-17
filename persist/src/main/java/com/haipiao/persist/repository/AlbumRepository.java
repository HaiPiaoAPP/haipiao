package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wangshun
 */
public interface AlbumRepository extends CrudRepository<Album, Integer> {

    /**
     * 获取用户收藏的专辑
     * @param followerId
     * @param pageable
     * @return
     */
    @Query(value = "select a.* " +
            "from album a " +
            "left join user_album_relation uar on a.album_id = uar.album_id " +
            "where uar.follower_id = :followerId " +
            "order by uar.create_ts desc", nativeQuery = true)
    Page<Album> findArticlesByFollowerIdForPage(@Param("followerId") Integer followerId, Pageable pageable);

}
