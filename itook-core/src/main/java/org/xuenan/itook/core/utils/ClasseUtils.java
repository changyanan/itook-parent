package org.xuenan.itook.core.utils;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class ClasseUtils {
    static final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Map<Method, String> cacheName = new HashMap();
    private static final Map<Method, Boolean> cacheState = new HashMap();

    public ClasseUtils() {
    }

    public static final String[] getMethodParamNames(final Method method) {
        return discoverer.getParameterNames(method);
    }

    public static final String[] getConstructorParamNames(final Constructor<?> ctor) {
        return discoverer.getParameterNames(ctor);
    }

    public static String getMethodName(Method method) {
        if (method == null) {
            return null;
        } else if (cacheName.containsKey(method)) {
            return (String)cacheName.get(method);
        } else {
            Class<?> clazz = method.getDeclaringClass();
            String methodName = method.getName();
            Class<?>[] types = method.getParameterTypes();
            String pname = ListUtils.n().a(types).list((t) -> t.getClass().getSimpleName()).join(",");
            String name = clazz.getName() + "." + methodName + "(" + pname + ")";
            cacheName.put(method, name);
            return name;
        }
    }

    public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotation) {
        if (method == null) {
            return false;
        } else if (cacheState.containsKey(method)) {
            return (Boolean)cacheState.get(method);
        } else {
            Class<?> targetClass = method.getDeclaringClass();
            Annotation classannotation = AnnotationUtils.findAnnotation(targetClass, annotation);
            Annotation methodannotation = AnnotationUtils.findAnnotation(method, annotation);
            boolean state = methodannotation != null || classannotation != null;
            cacheState.put(method, state);
            return state;
        }
    }
}
