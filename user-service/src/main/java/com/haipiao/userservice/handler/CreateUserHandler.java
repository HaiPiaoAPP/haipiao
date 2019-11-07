package com.haipiao.userservice.handler;

import com.google.gson.Gson;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.service.SessionService;
import com.haipiao.common.util.session.SessionToken;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.enums.Gender;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.userservice.application.UserController;
import com.haipiao.userservice.req.CreateUserRequest;
import com.haipiao.userservice.resp.CreateUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CreateUserHandler extends AbstractHandler<CreateUserRequest, CreateUserResponse> {
    public SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private final Gson gson;
    @Autowired
    private final RedisClientWrapper redisClient;
    @Autowired
    private final UserRepository userRepository;

    public CreateUserHandler(SessionService sessionService,
                             Gson gson,
                             RedisClientWrapper redisClient,
                             UserRepository userRepository) {
        super(CreateUserResponse.class, sessionService);
        this.gson = gson;
        this.redisClient = redisClient;
        this.userRepository = userRepository;
    }

    @Override
    public CreateUserResponse execute(CreateUserRequest req) throws AppException {
        User user = new User();


        // TODO: validate username e.g. no duplicates
        user.setUserName(req.getName());

        Gender userGender = Gender.findByCode(req.getGender());
        if (userGender == null) {
            CreateUserResponse resp = new CreateUserResponse(StatusCode.BAD_REQUEST);
            resp.setErrorMessage("invalid gender format");
            return resp;
        }
        user.setGender(userGender);

        Date date;
        try {
            date = formatter.parse(req.getBirthday());
            logger.debug("date={}", date);
        } catch (ParseException ex) {
            CreateUserResponse resp = new CreateUserResponse(StatusCode.BAD_REQUEST);
            resp.setErrorMessage("invalid date format");
            return resp;
        }
        user.setBirthday(date);

        user = userRepository.save(user);
        int id = user.getUserId();
        logger.debug("id is assigned after persisted to DB. id={}", id);

        logger.info("invalidate temporary session token. oldSessionToken={}", req.getOldSessionToken());
        redisClient.delete(req.getOldSessionToken());
        SessionToken newSessionToken = sessionService.createUserSession(id);

        CreateUserResponse resp = new CreateUserResponse(StatusCode.SUCCESS);
        CreateUserResponse.Data data = new CreateUserResponse.Data();
        resp.setData(data);
        data.setId(id);
        data.setSessionToken(newSessionToken.toString());
        return resp;
    }
}
