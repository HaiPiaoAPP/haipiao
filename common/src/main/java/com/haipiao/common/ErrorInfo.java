package com.haipiao.common;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.enums.StatusCode;

import java.io.Serializable;

public class ErrorInfo implements Serializable {

    @SerializedName("error_code")
    private final StatusCode statusCode;

    @SerializedName("message")
    private final String message;

    public ErrorInfo(StatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
            "statusCode=" + statusCode +
            ", message='" + message + '\'' +
            '}';
    }
}
