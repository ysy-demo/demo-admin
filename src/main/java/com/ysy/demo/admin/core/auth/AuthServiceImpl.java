package com.ysy.demo.admin.core.auth;

import com.ysy.demo.admin.core.auth.entity.AuthToken;
import com.ysy.demo.admin.core.auth.entity.AuthInfo;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.auth.util.JwtUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${app.cache.prefix:}")
    private String cachePrefix;

    @Value("${app.cache.auth-user-timeout:7200}")
    private long cacheTimeout;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public AuthInfo getAuthInfo(String token) {
        AuthToken tokenInfo = JwtUtils.getTokenInfo(token);
        if (tokenInfo == null || StringUtils.isEmpty(tokenInfo.getUsername())) {
            throw new AuthenticationException("Invalid token");
        }
        if (System.currentTimeMillis() > tokenInfo.getExpireTime().getTime()) {
            throw new AuthenticationException("Token timeout");
        }
        AuthInfo authInfo = getCacheAuthInfo(token);
        if (authInfo == null) {
            throw new AuthenticationException("Token timeout");
        }

        if (!JwtUtils.verify(token, tokenInfo.getUsername(), authInfo.getUser().getPassword())) {
            throw new AuthenticationException("Invalid token");
        }
        return authInfo;
    }

    @Override
    public String saveAuthInfo(AuthInfo authInfo) {
        AuthUser user = authInfo.getUser();
        String token = generateToken(user.getUsername(), user.getPassword());
        redisTemplate.opsForValue().set(getAuthUserCacheKey(token), authInfo, cacheTimeout, TimeUnit.SECONDS);
        return token;
    }

    private AuthInfo getCacheAuthInfo(String token) {
        return (AuthInfo) redisTemplate.opsForValue().get(getAuthUserCacheKey(token));
    }

    private String getAuthUserCacheKey(String token) {
        return cachePrefix + "token-" + token;
    }

    private String generateToken(String username, String password) {
        Date expireTime = new Date(System.currentTimeMillis() + cacheTimeout * 1000);
        return JwtUtils.sign(username, password, expireTime);
    }
}
