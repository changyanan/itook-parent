package org.xuenan.itook.core.utils.serialize;

import org.nustaq.serialization.FSTConfiguration;

import java.util.LinkedList;
import java.util.List;

public class FstUtils {
    private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
    private static final List<Class<?>> clazs = new LinkedList();

    public FstUtils() {
    }

    private static final void registerClass(Class<?> claz) {
        if (!clazs.contains(claz)) {
            clazs.add(claz);
            conf.registerClass(new Class[]{claz});
        }

    }

    public static final void registerClass(Class<?>... clazs) {
        Class[] var1 = clazs;
        int var2 = clazs.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Class<?> claz = var1[var3];
            registerClass(claz);
        }

    }

    public static final void register(Object... obj) {
        Object[] var1 = obj;
        int var2 = obj.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object object = var1[var3];
            registerClass(object.getClass());
        }

    }

    public static final <T> byte[] serialize(T obj) {
        register(obj);
        return conf.asByteArray(obj);
    }

    public static final <T> T deserialize(byte[] data) {
        return (T) conf.asObject(data);
    }
}
