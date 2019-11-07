package com.haipiao.common.enums;

public enum StatusCode {

    SUCCESS("success"),
    BAD_REQUEST("bad request body"),
    UNAUTHORIZED("unauthorized request"),
    FORBIDDEN("forbidden"),
    INTERNAL_SERVER_ERROR("internal server error"),
    THROTTLED("request throttled"),
    NOT_FOUND("entity requested not found");

    StatusCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    private String defaultMessage;

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
