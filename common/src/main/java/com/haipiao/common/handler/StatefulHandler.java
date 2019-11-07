package com.haipiao.common.handler;

import org.springframework.http.ResponseEntity;

/**
 * This handler requires session token existence.
 * @param <ReqT> input request type
 * @param <RespT> output response type
 */
public interface StatefulHandler<ReqT, RespT> {

    ResponseEntity<RespT> handle(String sessionToken, ReqT request);

}
