package com.haipiao.persist.enums;

import java.util.Arrays;

public enum ImageStatus {

    PENDING(0), PUBLISHED(1), DELETE_PENDING(2), DELETED(3);

    ImageStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ImageStatus findByCode(int code) {
        return Arrays.stream(ImageStatus.values())
            .filter(e -> e.getCode() == code)
            .findFirst()
            .orElse(null);
    }

    private int code;

}
