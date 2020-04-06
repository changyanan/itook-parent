package org.xuenan.itook.core.consts;

public interface CacheConst {
    static StringBuffer generate() {
        return new StringBuffer("globalegrow");
    }

    static String generateMethodCallCacheKey(String type, String method, Object... args) {
        StringBuffer rediskey = generate().append(":").append(type).append(":").append(method);
        Object[] var4 = args;
        int var5 = args.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Object object = var4[var6];
            rediskey.append("[");
            rediskey.append(object);
            rediskey.append("]");
        }

        return rediskey.toString();
    }

    static String generateMethodCacheCacheKey(String method, Object... args) {
        return generateMethodCallCacheKey("METHODCACHE", method, args);
    }

    static String generateDoHttpRequestCacheKey(String method, Object... args) {
        return generateMethodCallCacheKey("DOHTTPREQUEST", method, args);
    }
}
