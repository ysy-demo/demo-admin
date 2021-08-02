package com.ysy.demo.admin.system.enums;

import lombok.Getter;

@Getter
public enum SysMenuType {

    MENU(0), BUTTON(1);

    private Integer value;

    SysMenuType(Integer value) {
        this.value = value;
    }

}
