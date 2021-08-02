package com.ysy.demo.admin.core;

import com.alibaba.fastjson.JSONObject;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.auth.util.AuthInfoUtils;
import com.ysy.demo.admin.system.entity.SysLog;
import com.ysy.demo.admin.system.service.SysLogService;
import com.ysy.demo.admin.core.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Around(value = "@annotation(event)")
    public Object around(ProceedingJoinPoint point, Log event) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed();
            return result;
        } finally {
            AuthUser user = AuthInfoUtils.getUser();
            MethodSignature signature = (MethodSignature) point.getSignature();
            SysLog log = new SysLog();
            log.setMethod(point.getTarget().getClass().getName() + "." + signature.getName());
            log.setUsername(user.getUsername());
            log.setUserId(user.getId());
            log.setIp(SubjectContext.get().getIp());
            log.setTime(System.currentTimeMillis() - beginTime);
            log.setOperation(event.value());
            log.setParams(JSONObject.toJSONString(point.getArgs()));
            log.setResult(result == null ? "" : JSONObject.toJSONString(result));
            sysLogService.save(log);
        }
    }
}
