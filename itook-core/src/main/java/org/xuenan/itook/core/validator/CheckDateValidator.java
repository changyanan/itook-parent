package org.xuenan.itook.core.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xuenan.itook.core.utils.DateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.Date;

public class CheckDateValidator implements ConstraintValidator<CheckDateTimeFormat, Date> {
    private static Logger logger = LoggerFactory.getLogger(CheckDateValidator.class);
    private int min;
    private int max;

    public CheckDateValidator() {
    }

    public void initialize(CheckDateTimeFormat checkDateTimeFormat) {
        this.min = checkDateTimeFormat.min();
        this.max = checkDateTimeFormat.max();
    }

    public boolean isValid(Date param, ConstraintValidatorContext constraintValidatorContext) {
        try {
            long receTime = DateUtils.date2Int(param).longValue();
            long minTime = DateUtils.getTimestamp(-this.min);
            long maxTime = DateUtils.getTimestamp(this.max);
            return receTime >= minTime && receTime < maxTime;
        } catch (ParseException var9) {
            logger.error("【CheckDateValidator】时间转换异常", var9);
            return false;
        }
    }
}
