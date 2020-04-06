package org.xuenan.itook.core.utils;

import org.xuenan.itook.core.model.SysModel;

import java.util.*;

public final class ListUtils<T> extends SysModel {
    private static final long serialVersionUID = 1L;
    private final List<T> list = new LinkedList();

    private ListUtils() {
    }

    public static final <T> ListUtils<T> n() {
        return new ListUtils();
    }

    public static final <T> ListUtils<T> n(final Iterable<T> collection) {
        return (ListUtils<T>) n().a(collection);
    }

    public static final <T> ListUtils<T> n(final T... ts) {
        return (ListUtils<T>) n().a(ts);
    }

    public final ListUtils<T> a(final T t) {
        if (t != null) {
            this.list.add(t);
        }

        return this;
    }

    public final ListUtils<T> a(final T... ts) {
        if (ts != null) {
            T[] var2 = ts;
            int var3 = ts.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                T t = var2[var4];
                this.list.add(t);
            }
        }

        return this;
    }

    public final ListUtils<T> a(final Iterable<T> ts) {
        if (ts != null) {
            List var10001 = this.list;
            ts.forEach(var10001::add);
        }

        return this;
    }

    public final boolean cv(final T t) {
        return this.list.contains(t);
    }

    public final int s() {
        return this.list.size();
    }

    public final T g(final int index) {
        return this.list.get(index);
    }

    public final ListUtils<T> r(final int... indexs) {
        int[] var2 = indexs;
        int var3 = indexs.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            int index = var2[var4];
            this.list.remove(index);
        }

        return this;
    }

    public final ListUtils<T> r(final T t) {
        this.list.remove(t);
        return this;
    }

    public final ListUtils<T> r(final T... ts) {
        Object[] var2 = ts;
        int var3 = ts.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            T t = (T) var2[var4];
            this.list.remove(t);
        }

        return this;
    }

    public final ListUtils<T> c() {
        this.list.clear();
        return this;
    }

    public final List<T> to() {
        return this.list;
    }

    public final ListUtils<T> each(final ListUtils.EachIterable<? super T> eachIterable) {
        Iterator it = this.list.iterator();

        while (it.hasNext()) {
            T t = (T) it.next();
            eachIterable.accept(t);
        }

        return this;
    }

    public final <N> ListUtils<T> unique(ListUtils.UniqueIterable<T, N> uniqueIterable) {
        Map<N, T> map = new HashMap(this.s());
        this.each((t) -> {
            N key = uniqueIterable.accept(t);
            if (!map.containsKey(key)) {
                map.put(key, t);
            }

        });
        return n((Iterable) map.values());
    }

    public final <N> ListUtils<T> unique() {
        ListUtils<T> lu = n();
        this.each((t) -> {
            if (!lu.cv(t)) {
                lu.a(t);
            }

        });
        return lu;
    }

    public final <N extends Comparable<N>> ListUtils<T> order(final ListUtils.OrderBy<T, N> orderby) {
        this.list.sort((t1, t2) -> {
            return orderby.accept(t1).compareTo(orderby.accept(t2));
        });
        return this;
    }

    public final <N extends Comparable<N>> ListUtils<T> orderDesc(final ListUtils.OrderBy<T, N> orderby) {
        this.list.sort((t1, t2) -> {
            return -1 * orderby.accept(t1).compareTo(orderby.accept(t2));
        });
        return this;
    }

    public final <N extends Comparable<N>> T min(final ListUtils.MaxOrMin<? super T, N> maxOrMin) {
        return this.m(maxOrMin, -1);
    }

    public final <N extends Comparable<N>> T max(final ListUtils.MaxOrMin<? super T, N> maxOrMin) {
        return this.m(maxOrMin, 1);
    }

    private final <N extends Comparable<N>> T m(final ListUtils.MaxOrMin<? super T, N> maxOrMin, int status) {
        if (isEmpty(this.list)) {
            return null;
        } else {
            T mcv = null;
            N mom = null;
            Iterator var5 = this.list.iterator();

            while (var5.hasNext()) {
                T t = (T) var5.next();
                N cv = maxOrMin.accept(t);
                if (cv != null) {
                    if (mcv == null) {
                        mcv = t;
                        mom = cv;
                    } else if (cv.compareTo(mom) * status > 0) {
                        mcv = t;
                        mom = cv;
                    }
                }
            }

            return mcv;
        }
    }

    public final ListUtils<T> filter(final ListUtils.FilterIterable<? super T> filter) {
        this.list.removeIf((t) -> {
            return !filter.test(t);
        });
        return this;
    }

    public final <N> ListUtils<N> list(final ListUtils.ListNewList<? super T, N> lnl) {
        ListUtils<N> lu = n();
        this.each((t) -> {
            N n = lnl.accept(t);
            if (n != null) {
                lu.a(n);
            }

        });
        return lu;
    }

    public final <N> ListUtils<N> listNoF(final ListUtils.ListNewList<? super T, N> lnl) {
        ListUtils<N> lu = n();
        this.each((t) -> {
            lu.a(lnl.accept(t));
        });
        return lu;
    }

    public final <N> ListUtils<N> lists(final ListUtils.ListNewLists<? super T, N> lnl) {
        ListUtils<N> lu = n();
        this.each((t) -> {
            List<N> n = lnl.accept(t);
            if (isNotEmpty(n)) {
                lu.a((Iterable) n);
            }

        });
        return lu;
    }

    public final <K> MapUtils<K, List<T>> group(final ListUtils.GroupIterable<? super T, K> groupIterable) {
        MapUtils<K, List<T>> relust = MapUtils.n();
        this.each((t) -> {
            if (t != null) {
                K key = groupIterable.accept(t);
                if (key != null) {
                    List<T> it = relust.ck(key) ? (List) relust.g(key) : new LinkedList();
                    ((List) it).add(t);
                    relust.a(key, it);
                }
            }
        });
        return relust;
    }

    public final String join() {
        return this.join((String) null);
    }

    public final String join(final String seg) {
        if (this.list.size() == 0) {
            return "";
        } else if (this.list.size() == 1) {
            return String.valueOf(this.list.get(0));
        } else {
            StringBuffer ret = new StringBuffer();
            ret.append(this.list.get(0));

            for (int i = 1; i < this.list.size(); ++i) {
                T t = this.list.get(i);
                if (seg != null) {
                    ret.append(seg);
                }

                ret.append(t);
            }

            return ret.toString();
        }
    }

    public boolean isNotEmpty() {
        return !this.list.isEmpty();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public static final boolean isNotEmpty(final Collection<?>... collections) {
        Collection[] var1 = collections;
        int var2 = collections.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Collection<?> collection = var1[var3];
            if (collection == null || collection.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static final boolean isEmpty(final Collection<?>... args) {
        return !isNotEmpty(args);
    }

    @FunctionalInterface
    public interface UniqueIterable<T, N> {
        N accept(final T t);
    }

    @FunctionalInterface
    public interface OrderBy<T, N extends Comparable<N>> {
        N accept(final T t);
    }

    @FunctionalInterface
    public interface MaxOrMin<T, N extends Comparable<N>> {
        N accept(final T t);
    }

    @FunctionalInterface
    public interface ListNewLists<T, N> {
        List<N> accept(T t);
    }

    @FunctionalInterface
    public interface ListNewList<T, N> {
        N accept(T t);
    }

    @FunctionalInterface
    public interface FilterIterable<T> {
        boolean test(final T t);
    }

    @FunctionalInterface
    public interface EachIterable<T> {
        void accept(final T t);
    }

    @FunctionalInterface
    public interface GroupIterable<V, K> {
        K accept(final V v);
    }
}
