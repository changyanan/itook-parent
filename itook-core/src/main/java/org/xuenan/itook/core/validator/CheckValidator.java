package org.xuenan.itook.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {CheckValidatorImpl.class}
)
@Documented
public @interface CheckValidator {
    Class<? extends Validator> checkValid();

    String[] args() default {};

    String message() default "校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
