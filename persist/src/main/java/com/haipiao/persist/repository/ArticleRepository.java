package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ArticleRepository extends CrudRepository<Article, Integer> {

    /**
     * 根据id获取文章
     * @param articleId
     * @return
     */
    @Query(value = "select * from article where article_id = :articleId", nativeQuery = true)
    Article findByArticleId(@Param("articleId") int articleId);

    /**
     * 获取当前点赞数最多的6篇文章
     * @param cursor
     * @param limit
     * @return
     */
    @Query(value = "select * from article order by likes desc limit ?1, ?2", nativeQuery = true)
    List<Article> findArticlesOrderByLikes(int cursor, int limit);

    /**
     * 获取当前时间和前某小时之间点赞数最多的前6篇文章
     * @param beginTime
     * @param lastTime
     * @param cursor
     * @param limit
     * @return
     */
    @Query(value = "select * from article where create_ts between ?1 and ?2 order by likes desc limit ?3, ?4", nativeQuery = true)
    List<Article> findByCreateTsBetween(String beginTime, String lastTime, int cursor, int limit);


    @Query(value = "select * from article where user_id = :authorId and status = :status order by create_ts desc", nativeQuery = true)
    Page<Article> findArticlesByAuthorIdAndStatus(@Param("authorId") Integer authorId, @Param("status") Integer status, Pageable pageable);

    @Query(value = "select a.* " +
            "from article a " +
            "right join article_collect_relation ac on a.article_id = ac.article_id " +
            "where ac.collector_id = :collectorId and a.status = :status " +
            "order by ac.create_ts desc", nativeQuery = true)
    Page<Article> findArticlesByCollectorIdAndStatus(@Param("collectorId") Integer collectorId, @Param("status") int status, Pageable pageable);


    @Query(value = "select count(a.article_id) " +
            "from album_article_relation aar " +
            "join article a on aar.article_id = a.article_id " +
            "where aar.album_id = ?1",nativeQuery = true)
    int countByAlbumId(Integer albumId);

    @Override
    @Modifying
    @Query(value = "delete from article", nativeQuery = true)
    void deleteAll();
}
