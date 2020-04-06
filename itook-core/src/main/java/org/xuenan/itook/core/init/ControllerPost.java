package org.xuenan.itook.core.init;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
@FunctionalInterface
public interface ControllerPost {
    void apply(ProceedingJoinPoint point, Method method, Object relust, Throwable throwable);
}
