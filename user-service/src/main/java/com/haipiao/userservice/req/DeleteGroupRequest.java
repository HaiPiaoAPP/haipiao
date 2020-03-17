package com.haipiao.userservice.req;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.req.AbstractRequest;

/**
 * @Author: wangshun
 */
public class DeleteGroupRequest extends AbstractRequest {
    @SerializedName("id")
    private Integer id;

    public DeleteGroupRequest() {
    }

    public DeleteGroupRequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
