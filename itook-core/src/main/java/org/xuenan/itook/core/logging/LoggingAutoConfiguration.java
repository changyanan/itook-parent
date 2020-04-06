package org.xuenan.itook.core.logging;

import org.springframework.context.annotation.ComponentScan;
import org.xuenan.itook.core.permission.PermissionConfig;

@ComponentScan(
        basePackageClasses = {ServiceRunTimeAop.class, ControllerLogAop.class, RunTimeLog.class, PermissionConfig.class}
)
public class LoggingAutoConfiguration {
    public LoggingAutoConfiguration() {
    }
}
