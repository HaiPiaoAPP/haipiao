package com.haipiao.persist.repository;

import com.haipiao.persist.entity.UserGroup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangshun
 */
@Transactional
public interface UserGroupRepository extends CrudRepository<UserGroup, Integer> {
    /**
     * 根据创建者id与类型查询
     *
     * @param ownerId
     * @param type
     * @return
     */
    List<UserGroup> findUserGroupsByOwnerIdAndType(Integer ownerId, String type);

    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    List<UserGroup> findUserGroupsByType(String type);

    /**
     * 根据主键与ownerId查询校验
     * @param groupId
     * @param ownerId
     * @return
     */
    int countByGroupIdAndOwnerId(Integer groupId,Integer ownerId);

    /**
     * 自定义存储用户分组信息
     * @param groupId
     * @param ownerId
     * @param typeString
     */
    @Modifying
    @Query(value = "insert into user_group (group_id, owner_id, type) values (:groupId, :ownerId, :type)", nativeQuery = true)
    void saveByHandle(@Param("groupId") Integer groupId, @Param("ownerId") Integer ownerId, @Param("type") String type);
}
