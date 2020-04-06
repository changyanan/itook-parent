package org.xuenan.itook.core.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.Context;
import org.xuenan.itook.core.utils.ClasseUtils;

import java.lang.reflect.Method;

@Aspect
@Order(1)
@Component
public class ServiceRunTimeAop {
    public ServiceRunTimeAop() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceRunTime() {
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryRunTime() {
    }

    @Around("repositoryRunTime() || serviceRunTime()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        boolean now = Context.now();
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        String methodName = ClasseUtils.getMethodName(method);
        Object args = this.getParamObject(point.getArgs());
        RunTimeLog.bedin();

        Object var6;
        try {
            var6 = point.proceed();
        } finally {
            RunTimeLog.log(" 方法执行结束 {},[参数]:{}", new Object[]{methodName, args});
            if (now) {
                Context.remove();
            }

        }

        return var6;
    }

    private Object getParamObject(Object[] param) {
        return new Object() {
            public String toString() {
                StringBuffer stringBuffer = new StringBuffer();
                Object[] var2 = param;
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Object object = var2[var4];
                    stringBuffer.append(" , ");
                    stringBuffer.append(String.valueOf(object));
                }

                return stringBuffer.toString();
            }
        };
    }
}
