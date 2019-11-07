package com.haipiao.userservice.handler;

import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.userservice.req.GetUserRequest;
import com.haipiao.userservice.resp.GetUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.haipiao.common.enums.StatusCode.NOT_FOUND;
import static com.haipiao.common.enums.StatusCode.SUCCESS;

@Component
public class GetUserHandler extends AbstractHandler<GetUserRequest, GetUserResponse>  {
    @Autowired
    private final UserRepository userRepository;

    public GetUserHandler(SessionService sessionService,
                          UserRepository userRepository) {
        super(GetUserResponse.class, sessionService);
        this.userRepository = userRepository;
    }

    @Override
    public GetUserResponse execute(GetUserRequest req) {
        GetUserResponse.Data data = new GetUserResponse.Data();

        int userID = req.getId() != null ? req.getId() : req.getLoggedInUserId();
        Optional<User> optionalUser = userRepository.findById(userID);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            GetUserResponse resp = new GetUserResponse(NOT_FOUND);
            resp.setErrorMessage(String.format("user %s not found in DB", userID));
            return resp;
        }

        data.setId(user.getUserId());
        data.setName(user.getUserName());
        data.setGender(user.getGender().toString());
        data.setDescription(user.getSignature());
        data.setProfileImageUrl(user.getProfileImgUrl());
        data.setProfileImageUrlSmall(user.getProfileImgUrlSmall());
        data.setOrganization(user.getOrganization());
        // TODO: show better location
        data.setLocation(user.getCountry()+user.getCity());

        // TODO
        // data.setFollowersCount();
        // data.setTotalLikes();
        // data.setTotalCollects();

        // TODO: set level
        // data.setLevel();

        GetUserResponse resp = new GetUserResponse(SUCCESS);
        resp.setData(data);
        return resp;
    }
}
