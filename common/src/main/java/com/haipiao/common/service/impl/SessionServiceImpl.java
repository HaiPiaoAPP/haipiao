package com.haipiao.common.service.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.redis.RedisClientWrapper;
import com.haipiao.common.service.SessionService;
import com.haipiao.common.util.session.SessionToken;
import com.haipiao.common.util.session.SessionUtils;
import com.haipiao.common.util.session.UserSessionInfo;
import com.haipiao.persist.entity.User;
import com.haipiao.persist.entity.UserSession;
import com.haipiao.persist.repository.UserRepository;
import com.haipiao.persist.repository.UserSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class SessionServiceImpl implements SessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private final RedisClientWrapper redisClient;
    @Autowired
    private final UserSessionRepository userSessionRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final Gson gson;

    public SessionServiceImpl(RedisClientWrapper redisClient,
                              UserSessionRepository userSessionRepository,
                              UserRepository userRepository, Gson gson) {
        this.redisClient = redisClient;
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
        this.gson = gson;
    }

    @Override
    public UserSessionInfo validateSession(String sessionToken) throws AppException {
        UserSessionInfo userSessionInfo;
        try {
             userSessionInfo = Optional.ofNullable(redisClient.get(sessionToken))
                .map(cachedVal -> gson.fromJson(cachedVal, UserSessionInfo.class))
                .orElse(null);
        } catch (JsonSyntaxException ex) {
            throw new AppException(StatusCode.INTERNAL_SERVER_ERROR, ex);
        }

        // Session exists.
        if (userSessionInfo != null) {
            LOGGER.info("userSessionInfo={}", userSessionInfo);
            return userSessionInfo;
        }

        // Load persisted session.
        SessionToken parsedSessionToken = SessionUtils.parseSessionToken(sessionToken);
        UserSession persistedSession = userSessionRepository.findBySelector(parsedSessionToken.getSelector());
        Preconditions.checkNotNull(persistedSession.getValidatorDigest());
        byte[] validatorDigest = SessionUtils.getValidatorDigest(parsedSessionToken.getValidator());
        if (Arrays.equals(validatorDigest, persistedSession.getValidatorDigest())) {
            throw new AppException(StatusCode.UNAUTHORIZED);
        }

        userSessionInfo =  new UserSessionInfo();
        userSessionInfo.setUserId(persistedSession.getUserId());
        redisClient.set(sessionToken, gson.toJson(userSessionInfo));
        return userSessionInfo;
    }

    @Override
    public SessionToken createUserSession(String phone) throws AppException {
        SessionToken sessionToken = SessionUtils.generateSessionToken();
        // find user id by user's phone number
        User loggedInUser = userRepository.getUserByPhone(phone);
        // If can't find the user, create a temporary session token.
        if (loggedInUser == null) {
            try {
                redisClient.set(sessionToken.toString(), "{}"); // create
                LOGGER.debug("Created temporary session. sessionToken={}", sessionToken.toString());
            } catch (Exception ex) {
                throw new AppException(StatusCode.INTERNAL_SERVER_ERROR, ex);
            }
            return sessionToken;
        }
        // If we can find the user by phone number, create a session token and persist it to DB/Redis
        UserSessionInfo sessionInfo = new UserSessionInfo();
        sessionInfo.setUserId(loggedInUser.getUserId());
        // persist user session in Database/Redis
        try {
            redisClient.set(sessionToken.toString(), gson.toJson(sessionInfo));
            UserSession userSession = new UserSession();
            userSession.setUserId(loggedInUser.getUserId());
            userSession.setSelector(sessionToken.getSelector());
            userSession.setValidatorDigest(SessionUtils.getValidatorDigest(sessionToken.getValidator()));
            userSessionRepository.save(userSession);
        } catch (Exception ex) {
            throw new AppException(StatusCode.INTERNAL_SERVER_ERROR, ex);
        }
        return sessionToken;
    }

    @Override
    public SessionToken createUserSession(int id) throws AppException {
        SessionToken sessionToken = SessionUtils.generateSessionToken();
        UserSessionInfo sessionInfo = new UserSessionInfo();
        sessionInfo.setUserId(id);
        try {
            redisClient.set(sessionToken.toString(), gson.toJson(sessionInfo));
            UserSession userSession = new UserSession();
            userSession.setUserId(id);
            userSession.setSelector(sessionToken.getSelector());
            userSession.setValidatorDigest(SessionUtils.getValidatorDigest(sessionToken.getValidator()));
            userSessionRepository.save(userSession);
        } catch (Exception ex) {
            throw new AppException(StatusCode.INTERNAL_SERVER_ERROR, ex);
        }
        return sessionToken;
    }

}
