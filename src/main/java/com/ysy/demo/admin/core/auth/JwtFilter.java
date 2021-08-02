package com.ysy.demo.admin.core.auth;

import com.alibaba.fastjson.JSONObject;
import com.ysy.demo.admin.core.model.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            return executeLogin(request, response);
        }
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return super.getAuthzHeader(request) != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        try {
            String token = super.getAuthzHeader(request);
            JwtToken jwtToken = new JwtToken(token);
            getSubject(request, response).login(jwtToken);
        } catch (AuthenticationException e) {
            log.info("executeLogin login failed message={}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个 option请求，这里我们给 option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        response(response, "Please log in");
        return false;
    }

    private void response(ServletResponse response, String message) {
        log.warn("response message={}", message);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ResultEntity resultEntity = new ResultEntity(HttpStatus.UNAUTHORIZED.value(), message);
        try (PrintWriter out = httpResponse.getWriter()) {
            out.print(JSONObject.toJSONString(resultEntity));
            out.flush();
        } catch (IOException e) {
            log.error("response exception", e);
        }
    }
}
