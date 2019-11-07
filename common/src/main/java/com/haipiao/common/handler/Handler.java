package com.haipiao.common.handler;

import org.springframework.http.ResponseEntity;

/**
 * This handler doesn't require session token existence.
 * @param <ReqT> input request type
 * @param <RespT> output response type
 */
public interface Handler<ReqT, RespT> {

    ResponseEntity<RespT> handle(ReqT request);

}
