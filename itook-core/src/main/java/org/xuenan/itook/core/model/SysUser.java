package org.xuenan.itook.core.model;

import java.util.Optional;

public interface SysUser {

    Optional<SysUser> empty = Optional.empty();

    boolean isLogin();

    String getUserId();
}
