package com.haipiao.registration.req;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.req.AbstractRequest;

public class VerifySCRequest extends AbstractRequest {

    @SerializedName("code")
    private String securityCode;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("cell")
    private String cell;

    @SerializedName("type")
    private String type;

    public String getCountryCode() {
        return countryCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public String getCell() {
        return cell;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "VerifySCRequest{" +
            "securityCode='" + securityCode + '\'' +
            ", countryCode='" + countryCode + '\'' +
            ", cell='" + cell + '\'' +
            ", type='" + type + '\'' +
            '}';
    }
}
