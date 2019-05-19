package com.haipiao.persist.enums;

import java.util.Arrays;

public enum Gender {

    MALE(0), FEMALE(1), UNKNOWN(2);

    Gender(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Gender findByCode(int code) {
        return Arrays.stream(Gender.values())
            .filter(e -> e.getCode() == code)
            .findFirst()
            .orElse(null);
    }

    private final int code;

}
