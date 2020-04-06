package org.xuenan.itook.core.utils.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KryoUtil {
    private static final Kryo kryo = new Kryo();
    private static final Map<Class<?>, Registration> cachedSchema = new ConcurrentHashMap();

    public KryoUtil() {
    }

    public static Registration registerClass(Class<?> cls) {
        if (!cachedSchema.containsKey(cls)) {
            cachedSchema.put(cls, kryo.register(cls));
        }

        return (Registration)cachedSchema.get(cls);
    }

    public static Registration register(Object obj) {
        return registerClass(obj.getClass());
    }

    public static <T> byte[] serialize(T obj) {
        register(obj);
        Output output = new Output(1, 4096);

        byte[] var2;
        try {
            kryo.writeObject(output, obj);
            output.flush();
            var2 = output.toBytes();
        } finally {
            output.close();
        }

        return var2;
    }

    public static <T> T deserialize(byte[] data, Class<T> cls) {
        Input input = new Input(data);

        Object var3;
        try {
            var3 = kryo.readObject(input, registerClass(cls).getType());
        } finally {
            input.close();
        }

        return (T) var3;
    }

    public static <T> byte[] serializeAndType(T obj) {
        register(obj);
        Output output = new Output(1, 4096);

        byte[] var2;
        try {
            kryo.writeClassAndObject(output, obj);
            output.flush();
            var2 = output.toBytes();
        } finally {
            output.close();
        }

        return var2;
    }

    public static <T> T deserializeAndType(byte[] data) {
        Input input = new Input(data);

        Object var2;
        try {
            var2 = kryo.readClassAndObject(input);
        } finally {
            input.close();
        }

        return (T) var2;
    }

    static {
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }
}
