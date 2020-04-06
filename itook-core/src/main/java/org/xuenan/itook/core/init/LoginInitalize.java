package org.xuenan.itook.core.init;

import org.xuenan.itook.core.model.SysUser;

@FunctionalInterface
public interface LoginInitalize {
    SysUser findLoginUser();
}
