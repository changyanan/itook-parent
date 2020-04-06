package org.xuenan.itook.core.init;
@FunctionalInterface
public interface RequestPreService {
    boolean handle(Object handler);
}
