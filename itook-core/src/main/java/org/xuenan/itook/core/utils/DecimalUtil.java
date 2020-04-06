package org.xuenan.itook.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalUtil {
    public DecimalUtil() {
    }

    public static Double add(double x, double y) {
        return add(Double.toString(x), Double.toString(y));
    }

    public static Double add(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        return b1.add(b2).doubleValue();
    }

    public static Double subtract(double x, double y) {
        return subtract(Double.toString(x), Double.toString(y));
    }

    public static Double subtract(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        return b1.subtract(b2).doubleValue();
    }

    public static Double multiply(double x, double y) {
        return multiply(Double.toString(x), Double.toString(y));
    }

    public static Double multiply(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        return b1.multiply(b2).doubleValue();
    }

    public static Double divide(double x, double y) {
        return divide(Double.toString(x), Double.toString(y));
    }

    public static Double divide(String x, String y) {
        BigDecimal b1 = new BigDecimal(x);
        BigDecimal b2 = new BigDecimal(y);
        return b1.divide(b2, 8, 4).doubleValue();
    }

    public static Double round(double x, int scale) {
        return round(Double.toString(x), scale);
    }

    public static Double round(String x, int scale) {
        return (new BigDecimal(x)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
}
