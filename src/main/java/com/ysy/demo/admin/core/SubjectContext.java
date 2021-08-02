package com.ysy.demo.admin.core;

import org.springframework.core.NamedThreadLocal;

public class SubjectContext {

    private static NamedThreadLocal<Subject> threadLocal = new NamedThreadLocal<>("subject");

    public static void bind(Subject subject) {
        threadLocal.set(subject);
    }

    public static Subject get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
