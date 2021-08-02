package com.ysy.demo.admin.core.component;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Component
public class TransactionManagement {

    @Transactional(rollbackFor = Throwable.class)
    public <T, R> R handle(T t, Function<T, R> function) {
        return function.apply(t);
    }

}
