package com.haipiao.userservice.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjipeng
 */
public enum GetCategoryEnum {

    /**
     * 获取分类映射分类TYpe
     */
    DEFAULT("default"),
    HOT("hot"),
    MISC("misc");

    private String value;

    GetCategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean checkType(String type){
        GetCategoryEnum[] values = GetCategoryEnum.values();
        List<String> collect = Arrays.stream(values).map(GetCategoryEnum::getValue).collect(Collectors.toList());
        return collect.contains(type);
    }
}
