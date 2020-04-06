package org.xuenan.itook.core.permission;
@FunctionalInterface
public interface PermissionCheck {
    boolean check(String group, String name);
}
