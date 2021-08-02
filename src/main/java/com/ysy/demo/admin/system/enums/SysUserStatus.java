package com.ysy.demo.admin.system.enums;

import lombok.Getter;

@Getter
public enum SysUserStatus {

    LOCKED(0), NORMAL(1);

    private Integer value;

    SysUserStatus(Integer value) {
        this.value = value;
    }

}
