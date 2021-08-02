package com.ysy.demo.admin.core.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ysy.demo.admin.core.auth.entity.AuthToken;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtils {

    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.info("verify field {}", e.getMessage());
            return false;
        }
    }

    public static AuthToken getTokenInfo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return AuthToken.builder().username(jwt.getClaim("username").asString())
                    .expireTime(jwt.getExpiresAt()).build();
        } catch (Exception e) {
            log.warn("getTokenInfo exception token={}, message={}", token, e.getMessage());
            return null;
        }
    }

    public static String sign(String username, String secret, Date date) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
    }
}
