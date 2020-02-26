package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.constants.DeleteAndLikeConstants;
import com.haipiao.articleservice.dto.req.LikeArticleRequest;
import com.haipiao.articleservice.dto.resp.LikeArticleResponse;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.Article;
import com.haipiao.persist.entity.ArticleLikeRelation;
import com.haipiao.persist.repository.ArticleLikeRelationRepository;
import com.haipiao.persist.repository.ArticleRepository;
import com.haipiao.persist.repository.UserFollowingRelationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author wangjipeng
 */
@Component
public class DeleteAndLikeArticleHandler extends AbstractHandler<LikeArticleRequest, LikeArticleResponse> {

    public static final Logger LOG = LoggerFactory.getLogger(DeleteAndLikeArticleHandler.class);

    @Autowired
    private UserFollowingRelationRepository userFollowingRelationRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleLikeRelationRepository articleLikeRelationRepository;

    protected DeleteAndLikeArticleHandler(SessionService sessionService, UserFollowingRelationRepository userFollowingRelationRepository,
                                          ArticleRepository articleRepository, ArticleLikeRelationRepository articleLikeRelationRepository) {
        super(LikeArticleResponse.class, sessionService);
        this.userFollowingRelationRepository = userFollowingRelationRepository;
        this.articleRepository = articleRepository;
        this.articleLikeRelationRepository = articleLikeRelationRepository;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public LikeArticleResponse execute(LikeArticleRequest request) {
        Integer userId = request.getLoggedInUserId();
        int articleId = request.getId();
        Optional<Article> optional = articleRepository.findById(articleId);
        if (optional.isEmpty()){
            String errorMessage = "当前文章不存在...";
            LOG.info(errorMessage);
            LikeArticleResponse response = new LikeArticleResponse(StatusCode.NOT_FOUND);
            response.setErrorMessage(errorMessage);
            return response;
        }
        if (request.getType() == DeleteAndLikeConstants.LIKE_ARTICLE){
            // TODO queue选型确定，使用队列批量处理，此逻辑为点赞队列消费逻辑
            int followerCount = userFollowingRelationRepository.countByUserId(userId);
            ArticleLikeRelation relation = articleLikeRelationRepository.save(new ArticleLikeRelation(userId, articleId, followerCount));
            LOG.info("Save user:{} Likes Article:{} successfully", relation.getUserId(), relation.getArticleId());
        } else {
            // TODO queue选型确定，使用队列批量处理，此逻辑为取消点赞队列消费逻辑
            articleLikeRelationRepository.deleteByArticleIdAndUserId(articleId, userId);
            LOG.info("User:{} cancels likes for Article:{}", userId, articleId);
        }
        return new LikeArticleResponse(StatusCode.SUCCESS);
    }
}
