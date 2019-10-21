package com.haipiao.userservice.handler;

import com.haipiao.common.ErrorInfo;
import com.haipiao.common.enums.ErrorCode;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
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
    private final UserRepository userRepository;

    public CreateUserHandler(SessionService sessionService,
                             UserRepository userRepository) {
        super(sessionService);
        this.userRepository = userRepository;
    }

    @Override
    public CreateUserResponse execute(CreateUserRequest req) {
        User user = new User();
        CreateUserResponse resp = new CreateUserResponse();

        // TODO: validate username e.g. no duplicates
        user.setUserName(req.getName());

        Gender userGender = Gender.findByCode(req.getGender());
        if (userGender == null) {
            resp.setSuccess(false);
            resp.setErrorInfo(new ErrorInfo(ErrorCode.BAD_REQUEST, "invalid gender format"));
            return resp;
        }
        user.setGender(userGender);

        Date date;
        try {
            date = formatter.parse(req.getBirthday());
            logger.debug("date={}", date);
        } catch (ParseException ex) {
            resp.setSuccess(false);;
            resp.setErrorInfo(new ErrorInfo(ErrorCode.BAD_REQUEST, "invalid date format"));
            return resp;
        }
        user.setBirthday(date);

        user = userRepository.save(user);
        int id = user.getUserId();
        logger.debug(String.format("id %d assigned after persisted to DB", id));

        CreateUserResponse.Data data = new CreateUserResponse.Data();
        resp.setData(data);
        data.setId(id);
        resp.setSuccess(true);
        return resp;
    }
}
