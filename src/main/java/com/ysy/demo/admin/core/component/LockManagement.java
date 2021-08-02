package com.ysy.demo.admin.core.component;

import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.system.dto.SysResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

@Component
public class LockManagement {

    @Value("${app.cache.lock-prefix:}")
    private String cachePrefix;

    @Value("${app.cache.timeout:60}")
    private long timeout;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public <T, U> ResultEntity handle(String key, T t, U u, BiFunction<T, U, ResultEntity> function) {
        if (!lock(key)) {
            return SysResultEntity.LOCKED;
        }
        try {
            return function.apply(t, u);
        } finally {
            unLock(key);
        }
    }

    private boolean lock(String key) {
        return stringRedisTemplate.opsForValue()
                .setIfAbsent(cachePrefix + key, "1", timeout, TimeUnit.SECONDS);
    }

    private boolean unLock(String key) {
        return stringRedisTemplate.delete(cachePrefix + key);
    }

}
