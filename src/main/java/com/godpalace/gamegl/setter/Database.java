package com.godpalace.gamegl.setter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Database {
    String path();
    long autoSaveTime() default 0;
    TimeUnit autoSaveTimeUnit() default TimeUnit.MINUTES;
}
