package com.godpalace.gamegl.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ExitExecutor {
    private static final ExitExecutor instance;

    private ExitExecutor() {}

    static {
        instance = new ExitExecutor();
    }

    public static ExitExecutor getInstance() {
        return instance;
    }

    public void loadExitMethods(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof ExitInvokeMethod exitInvokeMethod) {
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                            if (exitInvokeMethod.methodType() == ExitInvokeMethod.MethodType.STATIC) {
                                method.invoke(null);
                            } else {
                                Object instance = clazz.getConstructor().newInstance();
                                method.invoke(instance);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }
            }
        }
    }
}
