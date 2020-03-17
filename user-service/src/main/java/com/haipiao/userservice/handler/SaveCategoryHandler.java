package com.haipiao.userservice.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.resp.AbstractResponse;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.Category;
import com.haipiao.persist.entity.Topic;
import com.haipiao.persist.entity.UserCategoryRelation;
import com.haipiao.persist.repository.CategoryRepository;
import com.haipiao.persist.repository.TopicRepository;
import com.haipiao.persist.repository.UserCategoryRelationRepository;
import com.haipiao.userservice.req.GetCategoryRequest;
import com.haipiao.userservice.req.SaveUserCategoryRequest;
import com.haipiao.userservice.resp.GetCategoryResponse;
import com.haipiao.userservice.resp.SaveUserCategoryResponse;
import com.haipiao.userservice.resp.dto.CategoryInfoDto;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author wangjipeng
 */
@Component
public class SaveCategoryHandler extends AbstractHandler<SaveUserCategoryRequest, SaveUserCategoryResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(SaveCategoryHandler.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserCategoryRelationRepository userCategoryRelationRepository;

    protected SaveCategoryHandler(SessionService sessionService,
                                  UserCategoryRelationRepository userCategoryRelationRepository) {
        super(SaveUserCategoryResponse.class, sessionService);
        this.userCategoryRelationRepository = userCategoryRelationRepository;
    }

    /**
     * 上传用户设置的5+ 个感兴趣主题 进行用户-主题 关联保存
     * - `BAD_REQUEST`: query parameter的类型不合法。
     * - `UNAUTHORIZED`: 用户未登录或者session token不合法。
     * - `INTERNAL_SERVER_ERROR`: 未知服务器错误.
     * @param request
     * @return
     */
    @Override
    protected SaveUserCategoryResponse execute(SaveUserCategoryRequest request) {
        if (request == null || request.getCategories().size() == 0){
            String errorMessage = "保存用户感兴趣主题请求参数为空! ";
            LOG.info(errorMessage);
            SaveUserCategoryResponse response = new SaveUserCategoryResponse(StatusCode.BAD_REQUEST);
            response.setErrorMessage(errorMessage);
            return response;
        }
        Iterable<Category> all = categoryRepository.findAll();
        List<Integer> existId = StreamSupport.stream(all.spliterator(), false).map(Category::getCategoryId).collect(Collectors.toList());
        List<Integer> categories = request.getCategories();
        int userId = request.getLoggedInUserId();
        List<UserCategoryRelation> list = categories.stream()
                .filter(existId::contains)
                .map(c -> new UserCategoryRelation(userId, c)).collect(Collectors.toList());
        userCategoryRelationRepository.saveAll(list);

        saveUserWithCategoryToRedis(userId, categories);
        return new SaveUserCategoryResponse(StatusCode.SUCCESS);
    }

    private void saveUserWithCategoryToRedis(int userId, List<Integer> categories){
        LOG.info("Save Into Redis");
    }
}
