package org.xuenan.itook.core.utils;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.web.config.WebProperties;
@Component
@EnableConfigurationProperties({WebProperties.class})
public class ThrowableUtils {
    private static WebProperties webProperties;

    public ThrowableUtils(WebProperties webProperties) {
        ThrowableUtils.webProperties = webProperties;
    }

    public static final String toStringIsDebug(Throwable throwable) {
        return !webProperties.isDebug() ? null : toString(throwable, 0);
    }

    public static final String toString(Throwable throwable) {
        return toString(throwable, 0);
    }

    private static final String toString(Throwable throwable, int depth) {
        if (throwable == null) {
            return "";
        } else {
            String throwableMessage = throwable.getMessage();
            StackTraceElement[] traces = throwable.getStackTrace();
            StringBuffer message = new StringBuffer("异常堆栈信息:");
            message.append("[");
            message.append(throwableMessage);
            message.append("]");

            for(int i = 0; i < traces.length && i < 50; ++i) {
                StackTraceElement trace = traces[i];
                message.append("\r\n,");
                message.append(trace.toString());
            }

            if (depth < 10) {
                message.append(toString(throwable.getCause(), depth + 1));
            }

            return message.toString();
        }
    }
}
