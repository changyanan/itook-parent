package org.xuenan.itook.core.utils;

public abstract class StringUtils {
    public static final String BLANK = "";

    public StringUtils() {
    }

    public static String joining(String[] format, Object... arguments) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < format.length; ++i) {
            if (format[i] != null) {
                sb.append(format[i]);
            }

            if (i < arguments.length && arguments[i] != null) {
                sb.append(arguments[i]);
            }
        }

        return sb.toString();
    }

    public static String joining(String name, Object... arguments) {
        if (arguments.length == 0) {
            return name;
        } else {
            StringBuilder sb = new StringBuilder(name);
            sb.append("[");
            sb.append(arguments[0]);
            sb.append(",");

            for(int i = 1; i < arguments.length; ++i) {
                sb.append(",");
                sb.append(arguments[i]);
            }

            sb.append("]");
            return sb.toString();
        }
    }

    public static String trimLimitLen(String content, int limit) {
        if (isEmpty(content)) {
            return "";
        } else {
            content = content.trim();
            return content.length() <= limit ? content : content.substring(0, limit);
        }
    }

    public static boolean isNotEmpty(String... args) {
        String[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String string = var1[var3];
            if (string == null || string.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static boolean isEmpty(String... args) {
        return !isNotEmpty(args);
    }

    public static String encode(String source) {
        return isEmpty(source) ? source : source.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&#039;").replaceAll("\"", "&quot;");
    }

    public static String decode(String source) {
        return isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#039;", "'").replaceAll("&quot;", "\"");
    }
}
