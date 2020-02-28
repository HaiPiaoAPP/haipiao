package com.haipiao.userservice.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.entity.UserFollowingRelation;
import com.haipiao.persist.entity.UserGroup;
import com.haipiao.persist.repository.UserFollowingRelationRepository;
import com.haipiao.persist.repository.UserGroupRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.userservice.enums.UserGroupTypeEnum;
import com.haipiao.userservice.req.DeleteGroupRequest;
import com.haipiao.userservice.req.GetUserGroupRequest;
import com.haipiao.userservice.resp.FolloweeUserResponse;
import com.haipiao.userservice.resp.GetGroupResponse;
import com.haipiao.userservice.resp.GetUserResponse;
import com.haipiao.userservice.resp.OperateResponse;
import com.haipiao.userservice.resp.dto.GetGroupDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.haipiao.common.enums.StatusCode.NOT_FOUND;
import static com.haipiao.common.enums.StatusCode.SUCCESS;


/**
 * @author wangshun
 */
@Component
public class DeleteGroupHandler extends AbstractHandler<DeleteGroupRequest, OperateResponse> {
    public static final Logger LOG = LoggerFactory.getLogger(DeleteGroupHandler.class);

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowingRelationRepository userFollowingRelationRepository;



    public DeleteGroupHandler(SessionService sessionService,
                              UserGroupRepository userGroupRepository,
                              UserRepository userRepository,
                              UserFollowingRelationRepository userFollowingRelationRepository) {
        super(OperateResponse.class, sessionService);
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.userFollowingRelationRepository = userFollowingRelationRepository;
    }

    @Override
    public OperateResponse execute(DeleteGroupRequest request) throws AppException {
        int userId = request.getLoggedInUserId();
        if(!checkUser(userId)){
            OperateResponse resp = new OperateResponse(NOT_FOUND);
            resp.setErrorMessage(String.format("user %s not found in DB", userId));
            return resp;
        }

        int groupId = request.getId();
        if(!checkUserGroup(userId,groupId)){
            OperateResponse resp = new OperateResponse(NOT_FOUND);
            resp.setErrorMessage(String.format("%s 不属于 %s 用户",groupId, userId));
            return resp;
        }
        userFollowingRelationRepository.updateGroupIdByGroupIdAndUserId(groupId,userId,1);
        // TODO  删除当前用户的指定分组： 分组id改为1(默认分组)后，为什么要删除当前用户 to wangshun
        //userRepository.deleteById(userId);
        return new OperateResponse(SUCCESS);
    }

    private boolean checkUser(int userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            return true;
        }
        return false;
    }

    private boolean checkUserGroup(int userId,int groupId){
        int count = userGroupRepository.countByGroupIdAndOwnerId(groupId,userId);
        if(count == 1) {
            return true;
        }
        return false;
    }

}
