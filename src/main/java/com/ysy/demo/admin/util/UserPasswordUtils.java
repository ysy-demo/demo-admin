package com.ysy.demo.admin.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordUtils {

    @Value("${app.user.password-login-salt:}")
    private String loginSalt;

    @Value("${app.user.password-save-salt:}")
    private String saveSalt;

    public String encrypt(String username, String password) {
        return DigestUtils.sha1Hex(username + password + saveSalt);
    }

    public String encryptOriginal(String username, String password) {
        password = DigestUtils.md5Hex(password + loginSalt);
        return DigestUtils.sha1Hex(username + password + saveSalt);
    }

    public String generatePassword() {
        return (int) (Math.random() * 1000000) + "";
    }

}
