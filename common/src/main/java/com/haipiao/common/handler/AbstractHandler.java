package com.haipiao.common.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.req.AbstractRequest;
import com.haipiao.common.resp.AbstractResponse;
import com.haipiao.common.service.SessionService;
import com.haipiao.common.util.session.UserSessionInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public abstract class AbstractHandler<ReqT extends AbstractRequest, RespT extends AbstractResponse>
    implements Handler<ReqT, RespT>, StatefulHandler<ReqT, RespT> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    protected final SessionService sessionService;
    private final Class<RespT> respClazz;

    protected AbstractHandler(Class<RespT> respClazz, SessionService sessionService) {
        this.respClazz =respClazz;
        this.sessionService = sessionService;
    }

    protected abstract RespT execute(ReqT request) throws AppException;

    @Override
    public ResponseEntity<RespT> handle(ReqT request) {
        return handle(null, request, false);
    }

    @Override
    public ResponseEntity<RespT> handle(String sessionToken, ReqT request) {
        return handle(sessionToken, request, true);
    }

    private ResponseEntity<RespT> handle(String sessionToken, ReqT request, boolean sessionRequired) {
        try {
            if (sessionRequired) {
                UserSessionInfo sessionInfo = sessionService.validateSession(sessionToken);
                request.setLoggedInUserId(sessionInfo.getUserId());
            }
            return ResponseEntity.ok(execute(request));
        } catch (Exception ex) {
            LOGGER.error("Exception happened while handling request.", ex);
            if (ex instanceof AppException) {
                return ResponseEntity.ok(constructErrorResponse(((AppException) ex).getStatusCode(), ex.getMessage()));
            }
            return ResponseEntity.ok(constructErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, ""));
        }
    }

    private RespT constructErrorResponse(StatusCode statusCode, String errorMessage) {
        try {
            RespT resp = respClazz.getDeclaredConstructor(StatusCode.class).newInstance(statusCode);
            resp.setErrorMessage(StringUtils.isNotBlank(errorMessage) ? errorMessage : statusCode.getDefaultMessage());
            return resp;
        } catch (Exception ex) {
            throw new RuntimeException("Fatal exception. Can't automatically construct errored response.", ex);
        }
    }

}
