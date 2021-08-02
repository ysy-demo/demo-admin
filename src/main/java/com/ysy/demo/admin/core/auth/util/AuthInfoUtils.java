package com.ysy.demo.admin.core.auth.util;

import com.ysy.demo.admin.core.auth.entity.AuthInfo;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import org.apache.shiro.SecurityUtils;

public class AuthInfoUtils {

    public static AuthUser getUser() {
        return getAuthInfo().getUser();
    }

    public static AuthInfo getAuthInfo() {
        return (AuthInfo) SecurityUtils.getSubject().getPrincipal();
    }
}
