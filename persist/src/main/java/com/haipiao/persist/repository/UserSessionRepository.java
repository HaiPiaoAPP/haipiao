package com.haipiao.persist.repository;

import com.haipiao.persist.entity.UserSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserSessionRepository extends CrudRepository<UserSession, Integer> {

    UserSession findBySelector(byte[] selector);

    /**
     * 清空数据
     */
    @Override
    @Modifying
    @Query(value = "delete from user_session", nativeQuery = true)
    void deleteAll();

}
