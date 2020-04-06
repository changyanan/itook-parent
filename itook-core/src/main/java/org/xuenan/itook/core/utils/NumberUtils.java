package org.xuenan.itook.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NumberUtils {
    private static final Logger log = LoggerFactory.getLogger(NumberUtils.class);

    public NumberUtils() {
    }

    public static boolean isNumber(String source) {
        if (StringUtils.isEmpty(new String[]{source})) {
            return false;
        } else {
            try {
                Long.parseLong(source);
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static boolean isDouble(String source) {
        if (StringUtils.isEmpty(new String[]{source})) {
            return false;
        } else {
            try {
                Double.parseDouble(source);
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static Long parseLong(String source) {
        if (StringUtils.isEmpty(new String[]{source})) {
            return null;
        } else {
            try {
                return Long.parseLong(source);
            } catch (Exception var2) {
                log.warn("将{}转化为Long失败", source);
                return null;
            }
        }
    }

    public static Integer parseInt(String source) {
        if (StringUtils.isEmpty(new String[]{source})) {
            return null;
        } else {
            try {
                return Integer.parseInt(source);
            } catch (Exception var2) {
                log.warn("将{}转化为Integer失败", source);
                return null;
            }
        }
    }

    public static Double parseDouble(String source) {
        if (StringUtils.isEmpty(new String[]{source})) {
            return null;
        } else {
            try {
                return Double.parseDouble(source);
            } catch (Exception var2) {
                log.warn("将{}转化为Double失败", source);
                return null;
            }
        }
    }

    public static Float parseFloat(String source) {
        if (StringUtils.isEmpty(new String[]{source})) {
            return null;
        } else {
            try {
                return Float.parseFloat(source);
            } catch (Exception var2) {
                log.warn("将{}转化为Float失败", source);
                return null;
            }
        }
    }
}
