package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<Topic, Integer> {
}
