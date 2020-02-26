package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    /**
     * 通过文章id分页查询该文章评论
     * @param id
     * @param pageable
     * @return
     */
    @Query(value = "select * from comment where article_id = :id order by comment_id", nativeQuery = true)
    Page<Comment> findByArticleIdAndLimit(@Param("id") int id, Pageable pageable);

    /**
     * 根据文章id获取问上评论总数量
     * @param id
     * @return
     */
    @Query(value = "select count(*) from comment where article_id = :id", nativeQuery = true)
    long findAllByArticleId(@Param("id") int id);
}
