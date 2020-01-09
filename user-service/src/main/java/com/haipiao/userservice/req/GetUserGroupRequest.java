package com.haipiao.userservice.req;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.req.AbstractRequest;

/**
 * @Author: wangshun
 */
public class GetUserGroupRequest extends AbstractRequest {
    @SerializedName("id")
    private Integer id;

    @SerializedName("type")
    private String type;

    public GetUserGroupRequest() {
    }

    public GetUserGroupRequest(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public GetUserGroupRequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
