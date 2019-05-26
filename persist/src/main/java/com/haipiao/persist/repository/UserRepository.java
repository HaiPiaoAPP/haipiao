package com.haipiao.persist.repository;

import com.haipiao.persist.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}