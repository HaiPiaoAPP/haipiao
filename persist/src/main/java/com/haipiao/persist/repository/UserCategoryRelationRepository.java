package com.haipiao.persist.repository;

import com.haipiao.persist.entity.UserCategoryRelation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wangjipeng
 */
public interface UserCategoryRelationRepository extends CrudRepository<UserCategoryRelation, Integer> {

    /**
     * 根据用户id查询改用户所有分类id
     * @param userId
     * @return
     */
    @Query(value = "select category_id from user_category_relation where user_id = :userId", nativeQuery = true)
    List<Integer> findByUserId(@Param("userId") int userId);
}
