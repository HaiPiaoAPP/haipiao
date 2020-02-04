package com.haipiao.common.enums;

import java.util.Arrays;

public enum SecurityCodeType {

    LOGIN("login"), CHANGE_PASSWORD("password");

    SecurityCodeType(String code) {
        this.code = code;
    }

    public static SecurityCodeType findByCode(String code) {
        return Arrays.stream(SecurityCodeType.values())
            .filter(sc -> code.equalsIgnoreCase(sc.getCode()))
            .findFirst()
            .orElse(null);
    }

    public String getCode() {
        return code;
    }

    private final String code;

}
