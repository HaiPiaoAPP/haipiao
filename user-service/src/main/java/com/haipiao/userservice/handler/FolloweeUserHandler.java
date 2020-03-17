package com.haipiao.userservice.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.entity.UserFollowingRelation;
import com.haipiao.persist.repository.UserFollowingRelationRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.userservice.req.FolloweeUserRequest;
import com.haipiao.userservice.resp.FolloweeUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * @author wangjipeng
 */
@Component
public class FolloweeUserHandler extends AbstractHandler<FolloweeUserRequest, FolloweeUserResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(FolloweeUserHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowingRelationRepository userFollowingRelationRepository;

    protected FolloweeUserHandler(SessionService sessionService,
                                  UserRepository userRepository,
                                  UserFollowingRelationRepository userFollowingRelationRepository) {
        super(FolloweeUserResponse.class, sessionService);
        this.userRepository = userRepository;
        this.userFollowingRelationRepository = userFollowingRelationRepository;
    }

    /**
     * - `BAD_REQUEST`: group_id或followee_id不存在。
     * - `UNAUTHORIZED`: 用户未登录或者session token不合法。
     * - `INTERNAL_SERVER_ERROR`: 未知服务器错误。
     * @param request
     * @return
     */
    @Override
    protected FolloweeUserResponse execute(FolloweeUserRequest request) {
        int userId = request.getLoggedInUserId();
        Optional<User> followUser = userRepository.findById(request.getFolloweeId());
        if (!followUser.isPresent()){
            String errorMessage = String.format("当前关注用户不存在, Id:%s", request.getFolloweeId());
            LOG.info(errorMessage);
            FolloweeUserResponse response = new FolloweeUserResponse(StatusCode.BAD_REQUEST);
            response.setErrorMessage(errorMessage);
            return response;
        }
        userFollowingRelationRepository.save(new UserFollowingRelation(followUser.get().getUserId(), userId, request.getGroupId()));

        saveFolloweeUserToRedis(userId, followUser.get().getUserId());
        return new FolloweeUserResponse(StatusCode.SUCCESS);
    }

    private void saveFolloweeUserToRedis(int followeeWithId, int userId){
        LOG.info("Save Into Redis");
    }
}
