package com.haipiao.persist.enums;

import java.util.Arrays;

public enum Gender {

    MALE("M"), FEMALE("F"), UNKNOWN("U");

    Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Gender findByCode(String code) {
        return Arrays.stream(Gender.values())
            .filter(e -> e.getCode().equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }

    private final String code;

}
