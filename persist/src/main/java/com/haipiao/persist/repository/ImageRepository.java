package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Integer> {
}
