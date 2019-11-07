package com.haipiao.userservice.resp;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.resp.AbstractResponse;

public class CreateUserResponse extends AbstractResponse<CreateUserResponse.Data> {

    public CreateUserResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public static class Data {
        @SerializedName("id")
        private int id;
        @SerializedName("session-token")
        private String sessionToken;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }
    }

}
