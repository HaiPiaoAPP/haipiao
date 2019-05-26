package com.haipiao.persist.repository;

import com.haipiao.persist.entity.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
}
