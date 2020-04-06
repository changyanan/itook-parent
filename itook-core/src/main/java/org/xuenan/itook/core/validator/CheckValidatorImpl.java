package org.xuenan.itook.core.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xuenan.itook.core.utils.SpringContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckValidatorImpl implements ConstraintValidator<CheckValidator, Object> {
    private static final Logger log = LoggerFactory.getLogger(CheckValidatorImpl.class);
    private Validator validator;
    private String[] args;
    private Class<? extends Validator> validatorClaz;

    public CheckValidatorImpl() {
    }

    public void initialize(CheckValidator constraintAnnotation) {
        this.validatorClaz = constraintAnnotation.checkValid();
        this.args = constraintAnnotation.args();
        this.validator = SpringContextHolder.getBean(this.validatorClaz);
        if (this.validator == null) {
            log.warn("spring上下文未找到校验类{}的实例，将使用创建实例", this.validatorClaz);

            try {
                this.validator = (Validator)this.validatorClaz.newInstance();
            } catch (IllegalAccessException | InstantiationException var3) {
                throw new RuntimeException("初始化校验类(" + this.validatorClaz + ")失败", var3);
            }
        }

    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            return this.validator.isValid(value, this.args);
        } catch (Exception var4) {
            log.info("校验类执行出错,{}", this.validatorClaz, var4);
            return false;
        }
    }
}
