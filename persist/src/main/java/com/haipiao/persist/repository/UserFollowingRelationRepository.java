package com.haipiao.persist.repository;

import com.haipiao.persist.entity.UserFollowingRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangjipeng
 */
@Transactional(rollbackFor = Throwable.class)
public interface UserFollowingRelationRepository extends CrudRepository<UserFollowingRelation, Integer> {

    /**
     * 获取此用户有多少粉丝
     * @param userId
     * @return long
     */
    @Query(value = "select count(*) from user_following_relation where user_id = :userId", nativeQuery = true)
    int countByUserId(@Param("userId") int userId);


    /**
     * 根据userId与followingUserId查询数量
     * @param userId
     * @param followingUserId
     * @return
     */
    int countByUserIdAndFollowingUserId(int userId, int followingUserId);

    /**
     * 获取所有关注此用户的用户Id
     * @param userId
     * @param pageable
     * @return
     */
    @Query(value = "select following_user_id from user_following_relation where user_id = :userId order by id desc", nativeQuery = true)
    Page<Integer> findUserFollowingIdsByUserId(@Param("userId")int userId, Pageable pageable);

    /**
     * 根据userId，groupIdBefore修改groupIdAfter
     * @param userId
     * @param groupIdBefore
     * @param groupIdAfter
     * @return
     */
    @Modifying
    @Query(value = "update user_following_relation set group_id = :groupIdAfter,update_ts = now() where user_id = :userId and group_id = :groupIdBefore",nativeQuery = true)
    void updateGroupIdByGroupIdAndUserId(@Param("groupIdBefore") int groupIdBefore, @Param("userId") int userId, @Param("groupIdAfter") int groupIdAfter);
}
