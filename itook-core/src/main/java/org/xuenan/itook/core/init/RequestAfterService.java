package org.xuenan.itook.core.init;

@FunctionalInterface
public interface RequestAfterService {
    void completion(Object handler, Exception ex);
}
