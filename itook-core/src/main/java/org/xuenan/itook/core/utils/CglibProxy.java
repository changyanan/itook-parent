package org.xuenan.itook.core.utils;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public abstract class CglibProxy {
    public CglibProxy() {
    }

    public static final <T> T create(Class<T> clazz, MethodInterceptor callback) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(callback);
        enhancer.setUseCache(false);
        enhancer.setUseFactory(false);
        enhancer.setClassLoader(clazz.getClassLoader());
        return (T) enhancer.create();
    }
}
