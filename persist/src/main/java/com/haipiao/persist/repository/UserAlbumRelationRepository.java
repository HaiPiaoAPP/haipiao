package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Album;
import com.haipiao.persist.entity.UserAlbumRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author wangshun
 */
public interface UserAlbumRelationRepository extends CrudRepository<UserAlbumRelation, Integer> {

    @Query(value = "select count(1) " +
            "from album_article_relation " +
            "where album_id = ?1",nativeQuery = true)
    int countByAlbumId(Integer albumId);
}
