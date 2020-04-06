package org.xuenan.itook.core.utils;

import org.xuenan.itook.core.exception.ExceptionLevel;
import org.xuenan.itook.core.model.SysModel;

import java.util.*;

public class MapUtils <K, V> extends SysModel {
    private static final long serialVersionUID = 1L;
    private final Map<K, V> map;

    private MapUtils() {
        this.map = new HashMap();
    }

    private MapUtils(final Map<K, V> map) {
        this();
        if (map != null && map.size() > 0) {
            this.map.putAll(map);
        }

    }

    public static final <K, V> MapUtils<K, V> n() {
        return new MapUtils();
    }

    public static final <K, V> MapUtils<K, V> n(final Map<K, V> map) {
        return new MapUtils(map);
    }

    public final MapUtils<K, V> a(final K key, final V value) {
        this.map.put(key, value);
        return this;
    }

    public final V g(final K key) {
        return this.map.get(key);
    }

    public final ListUtils<V> g(final K... keys) {
        ListUtils<V> lu = ListUtils.n();
        Object[] var3 = keys;
        int var4 = keys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            K k = (K) var3[var5];
            V v = this.g(k);
            lu.a(v);
        }

        return lu;
    }

    public final MapUtils<K, V> r(final K key) {
        this.map.remove(key);
        return this;
    }

    public final MapUtils<K, V> r(final K... keys) {
        Object[] var2 = keys;
        int var3 = keys.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            K key = (K) var2[var4];
            this.map.remove(key);
        }

        return this;
    }

    public final MapUtils<K, V> c() {
        this.map.clear();
        return this;
    }

    public final Map<K, V> to() {
        return this.map;
    }

    public final boolean ck(final K key) {
        return this.map.containsKey(key);
    }

    public final boolean cv(final V value) {
        return this.map.containsValue(value);
    }

    public final int s() {
        return this.map.size();
    }

    public final MapUtils<K, V> each(final MapUtils.EachMap<K, V> eachMap) {
        this.map.forEach(eachMap::accept);
        return this;
    }

    public final MapUtils<K, V> filter(final MapUtils.FilterMap<K, V> filter) {
        MapUtils<K, V> mu = n();
        this.each((k, v) -> {
            if (filter.test(k, v)) {
                mu.a(k, v);
            }

        });
        return mu;
    }

    public final <N> MapUtils<K, N> map(final MapUtils.MapNewMap<K, V, N> mnm) {
        MapUtils<K, N> mu = n();
        this.each((k, v) -> {
            N val = mnm.accept(k, v);
            if (val != null) {
                mu.a(k, val);
            }

        });
        return mu;
    }

    public final <NK> MapUtils<NK, V> mapKey(final MapUtils.MapNewMap<K, V, NK> mnm) {
        MapUtils<NK, V> mu = n();
        this.each((k, v) -> {
            NK nk = mnm.accept(k, v);
            if (nk != null) {
                mu.a(nk, v);
            }

        });
        return mu;
    }

    public final <T> ListUtils<T> array(final MapUtils.ArrayMap<K, V, T> am) {
        ListUtils<T> lu = ListUtils.n();
        this.each((k, v) -> {
            T t = am.accept(k, v);
            if (t != null) {
                lu.a(t);
            }

        });
        return lu;
    }

    public final <T> ListUtils<T> arrays(final MapUtils.ArraysMap<K, V, T> am) {
        ListUtils<T> lu = ListUtils.n();
        this.each((k, v) -> {
            lu.a(am.accept(k, v));
        });
        return lu;
    }

    public final <N> MapUtils<K, V> unique(MapUtils.UniqueMap<K, V, N> uniqueMap) {
        List<N> l = new LinkedList();
        return this.filter((k, v) -> {
            N n = uniqueMap.accept(k, v);
            if (l.contains(n)) {
                return false;
            } else {
                l.add(n);
                return true;
            }
        });
    }

    public static final boolean isNotEmpty(final Map<?, ?>... args) {
        Map[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Map<?, ?> map = var1[var3];
            if (map == null || map.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static final boolean isEmpty(final Map<?, ?>... args) {
        return !isNotEmpty(args);
    }

    @FunctionalInterface
    public interface UniqueMap<K, V, N> {
        N accept(final K k, final V v);
    }

    @FunctionalInterface
    public interface MapNewMap<K, V, T> {
        T accept(final K k, final V v);
    }

    @FunctionalInterface
    public interface ArraysMap<K, V, T> {
        Collection<T> accept(final K k, final V v);
    }

    @FunctionalInterface
    public interface ArrayMap<K, V, T> {
        T accept(final K k, final V v);
    }

    @FunctionalInterface
    public interface FilterMap<K, V> {
        boolean test(final K k, final V v);
    }

    @FunctionalInterface
    public interface EachMap<K, V> {
        void accept(final K k, final V v);
    }
}
