package com.haipiao.userservice.req;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.req.AbstractRequest;

/**
 * @Author: wangshun
 */
public class CreateGroupRequest extends AbstractRequest {
    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("group_name")
    private String groupName;

    public CreateGroupRequest() {
    }

    public CreateGroupRequest(Integer userId, String groupName) {
        this.userId = userId;
        this.groupName = groupName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
