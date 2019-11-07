package com.haipiao.common.exception;

import com.haipiao.common.enums.StatusCode;

public class AppException extends Exception {

    private StatusCode statusCode;

    public StatusCode getStatusCode() {
        return statusCode;
    }

    private AppException() {
        super();
    }

    public AppException(StatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public AppException(StatusCode statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public AppException(StatusCode statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}
