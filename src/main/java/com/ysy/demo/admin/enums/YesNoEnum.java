package com.ysy.demo.admin.enums;

import lombok.Getter;

@Getter
public enum YesNoEnum {

    YES(1), NO(0);

    private Integer value;

    YesNoEnum(Integer value) {
        this.value = value;
    }

    public static YesNoEnum get(boolean isTrue) {
        return isTrue ? YesNoEnum.YES : YesNoEnum.NO;
    }
}
