package com.godpalace.gamegl.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExitInvokeMethod {
    MethodType methodType();

    enum MethodType {
        STATIC, INSTANCE
    }
}
