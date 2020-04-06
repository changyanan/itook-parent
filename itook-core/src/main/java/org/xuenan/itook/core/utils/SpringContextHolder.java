package org.xuenan.itook.core.utils;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext applicationContext = null;

    public SpringContextHolder() {
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static void clearHolder() {
        applicationContext = null;
    }

    public void setApplicationContext(ApplicationContext appContext) {
        applicationContext = appContext;
    }

    public void destroy() throws Exception {
        clearHolder();
    }

    private static void assertContextInjected() {
        Assert.notNull(applicationContext, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.", new Object[0]);
    }
}
