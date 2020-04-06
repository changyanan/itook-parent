package org.xuenan.itook.core.init;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

@FunctionalInterface
public interface ControllerPre {
    void apply(ProceedingJoinPoint point, Method method);

}
