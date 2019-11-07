package com.haipiao.common.resp;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.enums.StatusCode;

import java.io.Serializable;
import java.util.StringJoiner;

public abstract class AbstractResponse<T> implements Serializable {

    @SerializedName("status_code")
    private final StatusCode statusCode;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("data")
    private T data;

    public AbstractResponse(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AbstractResponse.class.getSimpleName() + "[", "]")
            .add("statusCode=" + statusCode)
            .add("errorMessage='" + errorMessage + "'")
            .add("data=" + data)
            .toString();
    }
}
