package com.haipiao.userservice.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.*;
import com.haipiao.userservice.enums.RecommendationContextEnum;
import com.haipiao.userservice.handler.constants.LimitNumConstant;
import com.haipiao.userservice.handler.factory.*;
import com.haipiao.userservice.req.RecommendationRequest;
import com.haipiao.userservice.resp.RecommendationResponse;
import com.haipiao.userservice.resp.dto.RecommendationInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangjipeng
 */
@Component
public class RecommendationHandler extends AbstractHandler<RecommendationRequest, RecommendationResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowingRelationRepository userFollowingRelationRepository;

    @Autowired
    private ArticleRecommended articleRecommended;

    @Autowired
    private DefaultRecommended defaultRecommended;

    @Autowired
    private UserProfileRecommended userProfileRecommended;

    protected RecommendationHandler(SessionService sessionService,
                                    UserRepository userRepository,
                                    UserFollowingRelationRepository userFollowingRelationRepository,
                                    ArticleRecommended articleRecommended,
                                    DefaultRecommended defaultRecommended,
                                    UserProfileRecommended userProfileRecommended) {
        super(RecommendationResponse.class, sessionService);
        this.userRepository = userRepository;
        this.userFollowingRelationRepository = userFollowingRelationRepository;
        this.articleRecommended = articleRecommended;
        this.defaultRecommended = defaultRecommended;
        this.userProfileRecommended = userProfileRecommended;
    }

    /**
     * 推荐值得关注的用户
     * - `BAD_REQUEST`: query parameter名称或组合不合法。
     * - `UNAUTHORIZED`: 用户未登录或者session token不合法。
     * - `INTERNAL_SERVER_ERROR`: 未知服务器错误。
     * @param request
     * @return
     * @throws AppException
     */
    @Override
    protected RecommendationResponse execute(RecommendationRequest request) throws AppException {
        RecommendationResponse response = new RecommendationResponse(StatusCode.SUCCESS);
        int thisUserId = request.getLoggedInUserId();
        Optional<User> optionalUser = userRepository.findById(thisUserId);
        User thisUser = null;
        if (optionalUser.isPresent()){
            thisUser = optionalUser.get();
        }

        List<User> recommendedUserList = chooseRecommended(request.getContext())
                .recommendedUsers(thisUser, request);

        List<RecommendationInfoDto> responseList = recommendedUserList.stream()
                .filter(Objects::nonNull)
                .map(u -> new RecommendationInfoDto(u.getUserId(), u.getRealName(), u.getProfileImgUrl(), findUserFollowee(u.getUserId()), u.getSignature()))
                .collect(Collectors.toList());

        int cursor = Integer.parseInt(request.getCursor());
        int limit = request.getLimit() == 0 ? LimitNumConstant.DEFAULT_LIMIT : request.getLimit();
        boolean moreToFollow = false;
        if (responseList.size() > limit * cursor){
            moreToFollow = true;
            responseList = nextList(cursor, limit, responseList);
        }
        RecommendationResponse.Data data = new RecommendationResponse.Data(responseList, cursor, moreToFollow);
        response.setData(data);
        return response;
    }

    private List<RecommendationInfoDto> nextList(int cursor, int limit, List<RecommendationInfoDto> all){
        List<RecommendationInfoDto> list = new ArrayList<>();
        for (int i = (cursor - 1) * limit; i <= cursor * limit - 1; i++){
            list.add(all.get(i));
        }
        return list;
    }

    private int findUserFollowee(int userId){
        return userFollowingRelationRepository.countByUserId(userId);
    }

    public Recommended chooseRecommended(String context){
        Map<String, Recommended> map = new HashMap<>(8);
        map.put(RecommendationContextEnum.DEFAULT.getValue(), defaultRecommended);
        map.put(RecommendationContextEnum.ARTICLE.getValue(), articleRecommended);
        map.put(RecommendationContextEnum.USER_PROFILE.getValue(), userProfileRecommended);
        return map.get(context);
    }
}
