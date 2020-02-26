package com.haipiao.articleservice.handler;

import com.haipiao.articleservice.dto.req.GetArticleCommentsRequest;
import com.haipiao.articleservice.dto.resp.FollowerResponse;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.UserAlbumRelationRepository;
import com.haipiao.persist.repository.UserFollowingRelationRepository;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.persist.vo.FollowerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangshun
 */
@Component
public class GetUserFollowerHandler extends AbstractHandler<GetArticleCommentsRequest, FollowerResponse> {

    public static final Logger LOG = LoggerFactory.getLogger(GetUserFollowerHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowingRelationRepository userAlbumRelationRepository;


    protected GetUserFollowerHandler(SessionService sessionService,
                                     UserRepository userRepository,
                                     UserFollowingRelationRepository userAlbumRelationRepository) {
        super(FollowerResponse.class, sessionService);
        this.userRepository = userRepository;
        this.userAlbumRelationRepository = userAlbumRelationRepository;
    }

    @Override
    public FollowerResponse execute(GetArticleCommentsRequest request) throws AppException {
        FollowerResponse resp = new FollowerResponse(StatusCode.SUCCESS);
        Integer userId = request.getId();
        Pageable pageable = PageRequest.of(Integer.parseInt(request.getCursor()), request.getLimit());
        Page<User> userPage = userRepository.findFollowersByUserIdForPage(userId, pageable);
        List<User> users = userPage.getContent();
        if (CollectionUtils.isEmpty(users)) {
            resp.setErrorMessage(String.format("user %s not have followers", userId));
            return resp;
        }

        List<FollowerData> followerList = users.stream()
                .filter(Objects::nonNull)
                .map(a -> new FollowerData(a.getUserId(),a.getUserName(),a.getProfileImgUrl(),
                        userAlbumRelationRepository.countByUserId(a.getUserId()),
                        userAlbumRelationRepository.countByUserIdAndFollowingUserId(a.getUserId(),userId) > 0))
                .collect(Collectors.toList());

        resp.setData(new FollowerResponse.Data(followerList, (int) userPage.getTotalElements(),
                request.getCursor()+users.size(), userPage.getTotalPages() > Integer.parseInt(request.getCursor())));
        return resp;
    }
}
