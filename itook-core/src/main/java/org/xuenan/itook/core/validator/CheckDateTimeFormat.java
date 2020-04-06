package org.xuenan.itook.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {CheckDateValidator.class}
)
@Documented
public @interface CheckDateTimeFormat {
    String message() default "时间有误";

    int min() default 3;

    int max() default 3;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
