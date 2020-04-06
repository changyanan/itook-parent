package org.xuenan.itook.core.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xuenan.itook.core.Context;
import org.xuenan.itook.core.init.ControllerPost;
import org.xuenan.itook.core.init.ControllerPre;
import org.xuenan.itook.core.utils.ClasseUtils;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class ControllerLogAop implements ApplicationContextAware {
    private volatile Map<String, ControllerPre> preMaps;
    private volatile Map<String, ControllerPost> postMaps;
    private ApplicationContext applicationContext;

    public ControllerLogAop() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object restRecordSysLog(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        Context.isRest(true);
        return this.recordSysLog(point, method);
    }

    @Around("within(@org.springframework.stereotype.Controller *)")
    public Object otherRecordSysLog(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        Context.isRest(ClasseUtils.hasAnnotation(method, ResponseBody.class));
        return this.recordSysLog(point, method);
    }

    public Object recordSysLog(ProceedingJoinPoint point, Method method) throws Throwable {
        Context.put("ControllerMethod", method);
        boolean now = Context.now();
        this.pre(point, method);
        String methodName = ClasseUtils.getMethodName(method);
        RunTimeLog.bedin();

        Object var6;
        try {
            Object relust = point.proceed();
            this.post(point, method, relust, (Throwable)null);
            var6 = relust;
        } catch (Throwable var10) {
            this.post(point, method, (Object)null, var10);
            throw var10;
        } finally {
            class NamelessClass_1 {
                NamelessClass_1() {
                }

                public String toString() {
                    StringBuffer stringBuffer = new StringBuffer();
                    Object[] var2 = point.getArgs();
                    int var3 = var2.length;

                    for(int var4 = 0; var4 < var3; ++var4) {
                        Object object = var2[var4];
                        stringBuffer.append(" , ");
                        stringBuffer.append(String.valueOf(object));
                    }

                    return stringBuffer.toString();
                }
            }

            RunTimeLog.log("方法执行结束:{} ,[参数]:{}", new Object[]{methodName, new NamelessClass_1()});
            if (now) {
                Context.remove();
            }

        }

        return var6;
    }

    private void pre(ProceedingJoinPoint point, Method method) {
        if (this.preMaps == null) {
            this.preMaps = this.applicationContext.getBeansOfType(ControllerPre.class);
        }

        this.preMaps.values().forEach((cp) -> {
            cp.apply(point, method);
        });
    }

    private void post(ProceedingJoinPoint point, Method method, Object relust, Throwable throwable) {
        if (this.postMaps == null) {
            this.postMaps = this.applicationContext.getBeansOfType(ControllerPost.class);
        }

        this.postMaps.values().forEach((cp) -> {
            cp.apply(point, method, relust, throwable);
        });
    }
}
