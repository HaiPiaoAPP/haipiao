package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.dto.req.GetArticleCommentsRequest;
import com.haipiao.articleservice.dto.resp.ArticleResponse;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.ArticleLikeRelation;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.ArticleLikeRelationRepository;
import com.haipiao.persist.repository.ArticleRepository;
import com.haipiao.persist.repository.ImageRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.persist.vo.ArticleData;
import com.haipiao.persist.vo.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangshun
 */
@Component
public class GetUserCollectionHandler extends AbstractHandler<GetArticleCommentsRequest, ArticleResponse> {

    public static final Logger LOG = LoggerFactory.getLogger(GetUserCollectionHandler.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleLikeRelationRepository articleLikeRelationRepository;

    protected GetUserCollectionHandler(SessionService sessionService,
                                       ArticleRepository articleRepository,
                                       ImageRepository imageRepository,
                                       UserRepository userRepository,
                                       ArticleLikeRelationRepository articleLikeRelationRepository) {
        super(ArticleResponse.class, sessionService);
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.articleLikeRelationRepository = articleLikeRelationRepository;
    }

    @Override
    public ArticleResponse execute(GetArticleCommentsRequest request) throws AppException {
        ArticleResponse resp = new ArticleResponse(StatusCode.SUCCESS);
        int userId = request.getId();
        Pageable pageable = PageRequest.of(Integer.parseInt(request.getCursor()), request.getLimit());
        Page<Article> articlesPage = articleRepository.findArticlesByCollectorIdAndStatus(userId, 1, pageable);
        List<Article> articles = articlesPage.getContent();
        if (CollectionUtils.isEmpty(articles)) {
            resp.setErrorMessage(String.format("user %s not have collection article", userId));
            return resp;
        }

        List<ArticleData> articlesList = articles.stream()
                .filter(Objects::nonNull)
                .map(a -> new ArticleData(imageRepository.findFirstByArticleId(a.getArticleId(),0).getExternalUrl(),
                        a.getArticleId(), a.getTitle(), a.getLikes(), checkIsLike(a.getArticleId(), userId),
                        assemblerAuthor(userId)))
                .collect(Collectors.toList());

        resp.setData(new ArticleResponse.Data(articlesList, (int) articlesPage.getTotalElements(),
                request.getCursor()+articles.size(), articlesPage.getTotalPages() > Integer.parseInt(request.getCursor())));
        return resp;
    }

    /**
     * 根据userId获取Author
     * @param authorId
     * @return
     */
    public Author assemblerAuthor(int authorId) {
        Optional<User> optionalUser = userRepository.findById(authorId);
        User user = optionalUser.isEmpty() ? null : optionalUser.get();
        return user == null ? null : new Author(user.getUserId(), user.getUserName(), user.getProfileImgUrl());
    }

    /**
     * 根据userId与articleId查询是否点赞
     * @param userId
     * @param articleId
     * @return
     */
    public boolean checkIsLike(int userId, int articleId) {
        List<ArticleLikeRelation> likeRelations = articleLikeRelationRepository.findByArticleIdAndUserId(articleId, userId);
        return likeRelations.size() > 0;
    }
}
