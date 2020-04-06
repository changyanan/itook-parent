package org.xuenan.itook.core.model;

import org.xuenan.itook.core.exception.ExceptionLevel;

public interface Entity<T> {
    boolean getSuccess();

    T getData();

    int getCode();

    String getMessage();

    ExceptionLevel getLevel();

    long getCurtime();
}
