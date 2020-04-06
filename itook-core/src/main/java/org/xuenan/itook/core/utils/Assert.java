package org.xuenan.itook.core.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.xuenan.itook.core.exception.GlobalException;
import org.xuenan.itook.core.exception.GlobalExceptionStatus;

import java.util.Collection;
import java.util.Map;

public abstract class Assert {
    public Assert() {
    }

    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void isNull(Object object, String message, Object... args) {
        if (object != null) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void notNull(Object object, String message, Object... args) {
        if (object == null) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void hasLength(String text, String message, Object... args) {
        if (!org.springframework.util.StringUtils.hasLength(text)) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void hasText(String text, String message, Object... args) {
        if (!org.springframework.util.StringUtils.hasText(text)) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void doesNotContain(String textToSearch, String substring, String message, Object... args) {
        if (org.springframework.util.StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void notEmpty(Object[] array, String message, Object... args) {
        if (ObjectUtils.isEmpty(array)) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void noNullElements(Object[] array, String message, Object... args) {
        if (array != null) {
            Object[] var3 = array;
            int var4 = array.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Object element = var3[var5];
                if (element == null) {
                    GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
                }
            }
        }

    }

    public static void notEmpty(Collection<?> collection, String message, Object... args) {
        if (CollectionUtils.isEmpty(collection)) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void notEmpty(Map<?, ?> map, String message, Object... args) {
        if (CollectionUtils.isEmpty(map)) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void state(boolean expression, String message, Object... args) {
        if (!expression) {
            GlobalException.info(GlobalExceptionStatus.INVALID_PARAMETER.getCode(), message, args);
        }

    }

    public static void notZero(Integer num, String message, Object... args) {
        isTrue(num != null && num != 0, message, args);
    }

    public static <A extends Comparable<B>, B extends Comparable<A>> void isGreater(A a, B b, String message, Object... args) {
        isTrue(a != null && b != null && a.compareTo(b) > 0, message, args);
    }

    public static void isEquals(Object a, Object b, String message, Object... args) {
        isTrue(a == null && b == null || a != null && a.equals(b), message, args);
    }

    public static void notEquals(Object a, Object b, String message, Object... args) {
        isTrue(a == null && b != null || a != null && !a.equals(b), message, args);
    }
}
