package com.ysy.demo.admin.core.auth;

import com.ysy.demo.admin.core.auth.entity.AuthInfo;

public interface AuthService {

    AuthInfo getAuthInfo(String username);

    String saveAuthInfo(AuthInfo authInfo);
}
