package org.xuenan.itook.core.utils.serialize;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtostuffUtil {
    private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap();

    public ProtostuffUtil() {
    }

    public static <T> Schema<T> registerClass(Class<T> cls) {
        if (!cachedSchema.containsKey(cls)) {
            cachedSchema.put(cls, RuntimeSchema.createFrom(cls));
        }

        return (Schema)cachedSchema.get(cls);
    }

    public static <T> Schema<T> register(T obj) {
        return (Schema<T>) registerClass(obj.getClass());
    }

    public static <T> byte[] serialize(T obj) {
        LinkedBuffer buffer = LinkedBuffer.allocate(512);

        byte[] var3;
        try {
            Schema<T> schema = register(obj);
            var3 = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception var7) {
            throw new IllegalStateException(var7.getMessage(), var7);
        } finally {
            buffer.clear();
        }

        return var3;
    }

    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            Schema<T> schema = registerClass(cls);
            T message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception var4) {
            throw new IllegalStateException(var4.getMessage(), var4);
        }
    }
}
