package com.haipiao.registration.resp;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Until;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.resp.AbstractResponse;

public class VendSCResponse extends AbstractResponse {

    @Until(1.0)
    @SerializedName("security_code")
    private String securityCode;

    public VendSCResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
