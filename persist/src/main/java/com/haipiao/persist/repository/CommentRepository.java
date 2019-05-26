package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
