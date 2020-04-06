package org.xuenan.itook.core.permission;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String value() default "";

    String[] group() default {};
}
