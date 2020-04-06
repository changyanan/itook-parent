package org.xuenan.itook.core.utils;

public class ObjectUtils {
    public ObjectUtils() {
    }

    public static boolean isNotNull(Object... args) {
        Object[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object object = var1[var3];
            if (object == null) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNull(Object... args) {
        return !isNotNull(args);
    }

    public static boolean isNotNull(ObjectUtils.ObjectSteam... args) {
        ObjectUtils.ObjectSteam[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ObjectUtils.ObjectSteam objectSteam = var1[var3];
            if (objectSteam.apply() == null) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNull(ObjectUtils.ObjectSteam... args) {
        return !isNotNull(args);
    }

    @FunctionalInterface
    public interface ObjectSteam {
        Object apply();
    }
}
