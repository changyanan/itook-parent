package org.xuenan.itook.core.limiter;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {
    double value() default 1000.0D;
}