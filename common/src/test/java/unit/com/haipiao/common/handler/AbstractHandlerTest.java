package com.haipiao.common.handler;

import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.exception.AppException;
import com.haipiao.common.req.AbstractRequest;
import com.haipiao.common.resp.AbstractResponse;
import com.haipiao.common.service.SessionService;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class AbstractHandlerTest {

    static class SimpleRequest extends AbstractRequest {
    }

    static class SimpleResponse extends AbstractResponse<String> {
        public SimpleResponse(StatusCode statusCode) {
            super(statusCode);
        }
    }

    static class Handler extends AbstractHandler<SimpleRequest, SimpleResponse> {

        Handler(Class<SimpleResponse> respClazz, SessionService sessionService) {
            super(respClazz, sessionService);
        }

        @Override
        protected SimpleResponse execute(SimpleRequest request) throws AppException {
            return new SimpleResponse(StatusCode.SUCCESS);
        }
    }

    static class BadHandler extends AbstractHandler<SimpleRequest, SimpleResponse> {

        BadHandler(Class<SimpleResponse> respClazz,
                             SessionService sessionService) {
            super(respClazz, sessionService);
        }

        @Override
        protected SimpleResponse execute(SimpleRequest request) throws AppException {
            throw new AppException(StatusCode.UNAUTHORIZED, "Not a valid request", new Exception());
        }
    }

    static class VeryBadHandler extends AbstractHandler<SimpleRequest, SimpleResponse> {

        VeryBadHandler(Class<SimpleResponse> respClazz,
                             SessionService sessionService) {
            super(respClazz, sessionService);
        }

        @Override
        protected SimpleResponse execute(SimpleRequest request) throws AppException {
            throw new RuntimeException();
        }
    }

    @Test
    public void testHappyPath() {
        Handler h = new Handler(SimpleResponse.class, null);
        ResponseEntity<SimpleResponse> resp = h.handle(new SimpleRequest());
        SimpleResponse body = resp.getBody();
        assertEquals(StatusCode.SUCCESS, body.getStatusCode());
    }

    @Test
    public void testAppExceptionThrown() {
        BadHandler h = new BadHandler(SimpleResponse.class, null);
        ResponseEntity<SimpleResponse> resp = h.handle(new SimpleRequest());
        SimpleResponse body = resp.getBody();
        assertEquals(StatusCode.UNAUTHORIZED, body.getStatusCode());
    }

    @Test
    public void testOtherExceptionThrown() {
        VeryBadHandler h = new VeryBadHandler(SimpleResponse.class, null);
        ResponseEntity<SimpleResponse> resp = h.handle(new SimpleRequest());
        SimpleResponse body = resp.getBody();
        assertEquals(StatusCode.INTERNAL_SERVER_ERROR, body.getStatusCode());
    }

}
