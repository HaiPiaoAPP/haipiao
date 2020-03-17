package com.haipiao.articleservice.handler.factory;

import com.haipiao.articleservice.dto.req.RecommendationArticleRequest;
import com.haipiao.articleservice.dto.resp.RecommendationArticleResponse;
import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.ArticleLikeRelation;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.ArticleLikeRelationRepository;
import com.haipiao.persist.repository.ArticleRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.persist.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangjipeng
 */
public abstract class AbstractRecommendationArticle implements RecommendationArticle {

    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractRecommendationArticle.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleLikeRelationRepository articleLikeRelationRepository;

    public List<RecommendationArticleResponse.Data.ArticleData> assemblerDate(RecommendationArticleRequest request) {
        List<Article> articles = getArticlesByContext(request);
        if (articles == null || articles.size() <= 0){
            LOGGER.info("{}当前推荐情景无文章数据, 返回点赞数最多前10篇", request.getContext());
            articles = articleRepository.findArticlesOrderByLikes(PageUtil.cursor(request.getCursor()), PageUtil.limit(request.getLimit()));
        }
        return articles.stream()
                .filter(Objects::nonNull)
                .map(a -> new RecommendationArticleResponse.Data.ArticleData("", a.getArticleId(), a.getTitle(), a.getLikes(), checkIsLike(a.getArticleId(), request.getLoggedInUserId()), assemblerAuthor(a.getAuthorId())))
                .collect(Collectors.toList());
    }

    private RecommendationArticleResponse.Data.ArticleData.Author assemblerAuthor(int authorId){
        Optional<User> optionalUser = userRepository.findById(authorId);
        User user = optionalUser.isEmpty() ? null : optionalUser.get();
        return user == null ? null : new RecommendationArticleResponse.Data.ArticleData.Author(user.getUserId(), user.getUserName(), user.getProfileImgUrl());
    }

    private boolean checkIsLike(int userId, int articleId){
        List<ArticleLikeRelation> likeRelations = articleLikeRelationRepository.findByArticleIdAndUserId(articleId, userId);
        return likeRelations.size() > 0;
    }
}
