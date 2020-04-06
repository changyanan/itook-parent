package org.xuenan.itook.core.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.xuenan.itook.core.Context;
import org.xuenan.itook.core.exception.GlobalException;
import org.xuenan.itook.core.exception.GlobalExceptionStatus;
import org.xuenan.itook.core.init.ControllerPre;

import java.lang.reflect.Method;
@Configuration
public class PermissionConfig {
    @Autowired(
            required = false
    )
    private PermissionCheck permissionCheck;

    public PermissionConfig() {
    }

    private boolean check(Method method, Class<?> thisClass) {
        Permission permissionClaz = (Permission) AnnotationUtils.findAnnotation(thisClass, Permission.class);
        Permission permissionMethod = (Permission)AnnotationUtils.findAnnotation(method, Permission.class);
        if (permissionClaz == null && permissionMethod == null) {
            return true;
        } else {
            String[] groups = null;
            String name = null;
            if (permissionClaz != null) {
                groups = permissionClaz.group();
                name = permissionClaz.value();
            }

            if (permissionMethod != null) {
                groups = permissionMethod.group();
                name = permissionMethod.value();
            }

            int var8 = groups.length;
            byte var9 = 0;
            if (var9 < var8) {
                String group = groups[var9];
                return this.permissionCheck.check(group, name);
            } else {
                return true;
            }
        }
    }

    @Bean
    ControllerPre controllerPre() {
        GlobalException globalException = new GlobalException(GlobalExceptionStatus.UNAUTHORIZED);
        return (point, method) -> {
            if (this.permissionCheck != null && Context.getRequest() != null && !this.check(method, point.getThis().getClass())) {
                throw globalException;
            }
        };
    }
}
