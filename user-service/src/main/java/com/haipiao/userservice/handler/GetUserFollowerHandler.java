package com.haipiao.userservice.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.UserFollowingRelationRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.persist.utils.PageUtil;
import com.haipiao.userservice.req.GetUserFollowerRequest;
import com.haipiao.userservice.resp.GetUserFollowerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author wangjipeng
 */
@Component
public class GetUserFollowerHandler extends AbstractHandler<GetUserFollowerRequest, GetUserFollowerResponse> {

    public static final Logger LOGGER = LoggerFactory.getLogger(GetUserFollowerHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowingRelationRepository userFollowingRelationRepository;

    protected GetUserFollowerHandler(SessionService sessionService,
                                     UserRepository userRepository,
                                     UserFollowingRelationRepository userFollowingRelationRepository) {
        super(GetUserFollowerResponse.class, sessionService);
        this.userRepository = userRepository;
        this.userFollowingRelationRepository = userFollowingRelationRepository;
    }

    @Override
    public GetUserFollowerResponse execute(GetUserFollowerRequest request) throws AppException {
        GetUserFollowerResponse response = new GetUserFollowerResponse(StatusCode.SUCCESS);

        int cursor = PageUtil.cursor(request.getCursor());
        int limit = PageUtil.limit(request.getLimit());
        List<Integer> followingIds = userFollowingRelationRepository.findUserFollowingIdsByUserId(request.getId(), cursor, limit);
        if (followingIds == null || followingIds.size() <= 0){
            String errorMessage = String.format("%s: 当前用户无粉丝", request.getId());
            LOGGER.info(errorMessage);
            response = new GetUserFollowerResponse(StatusCode.NOT_FOUND);
            response.setErrorMessage(errorMessage);
            return response;
        }

        int totalCount = getTotalCount(request.getId());
        response.setData(new GetUserFollowerResponse.Data(getFollowerList(followingIds, request.getId()), totalCount, String.valueOf(limit + cursor), cursor < totalCount));
        return response;
    }

    private List<GetUserFollowerResponse.Data.Follower> getFollowerList(List<Integer> followingIds, int thisUserId){
        Iterable<User> users = userRepository.findAllById(followingIds);
        return StreamSupport.stream(users.spliterator(), false)
                .filter(Objects::nonNull)
                .map(u -> new GetUserFollowerResponse.Data.Follower(u.getUserId(), u.getUserName(), u.getProfileImgUrl(), getTotalCount(u.getUserId()), checkThisUserIsFollower(u.getUserId(), thisUserId)))
                .collect(Collectors.toList());
    }

    private boolean checkThisUserIsFollower(int followerId, int thisUserId){
        int i = userFollowingRelationRepository.countByUserIdAndFollowingUserId(followerId, thisUserId);
        return i != 0;
    }

    private int getTotalCount(int id){
        return userFollowingRelationRepository.countByUserId(id);
    }
}
