package com.ysy.demo.admin.core;

import com.ysy.demo.admin.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestAdvice {

    @Autowired
    private HttpServletRequest request;

    @Pointcut("(@annotation(org.springframework.web.bind.annotation.RequestMapping) "
            + "|| @annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void request() {
    }

    @Around("request()")
    public Object interception(ProceedingJoinPoint joinPoint) throws Throwable {
        String uri = request.getRequestURI();
        StringBuilder logMsg = new StringBuilder(uri).append(" ").append(request.getMethod());
        if (joinPoint.getArgs().length > 0) {
            logMsg.append(" @P");
            for (Object arguments : joinPoint.getArgs()) {
                logMsg.append(" ").append(arguments);
            }
        }

        log.info(logMsg.toString());
        logMsg = new StringBuilder(uri);
        long start = System.currentTimeMillis();
        try {
            bindSubjectContext(request);
            Object value = joinPoint.proceed();
            if (value != null) {
                logMsg.append(" @R ").append(value);
            }
            return value;
        } finally {
            logMsg.append(" @C=").append(SubjectContext.get());
            long end = System.currentTimeMillis();
            logMsg.append(" @T ").append(end - start);
            log.info(logMsg.toString());
            SubjectContext.clear();
        }
    }

    private void bindSubjectContext(HttpServletRequest request) {
        Subject subject = Subject.builder().build();
        subject.setIp(IpUtils.getIpAddr(request));
        SubjectContext.bind(subject);
    }

}
