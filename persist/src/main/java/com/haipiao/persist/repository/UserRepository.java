package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    User getUserByPhone(String phone);

    /**
     * 获取粉丝最多的前100位
     * @return
     */
    @Query(value = "select * from hp_user where user_id in " +
            "(select user_id from user_following_relation GROUP BY user_id order by count(user_id) desc limit 100)", nativeQuery = true)
    List<User> getUserByFansCount();

    @Query(value = "select u.* " +
            "from user_following_relation ufr " +
            "left join hp_user u on ufr.following_user_id = u.user_id " +
            "where ufr.user_id = :userId " +
            "order by ufr.create_ts desc",nativeQuery = true)
    Page<User> findFollowersByUserIdForPage(@Param("userId") Integer userId, Pageable pageable);


}