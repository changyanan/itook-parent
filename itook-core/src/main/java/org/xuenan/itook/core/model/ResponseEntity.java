package org.xuenan.itook.core.model;

import org.xuenan.itook.core.exception.ExceptionLevel;
import org.xuenan.itook.core.exception.ExceptionStatus;
import org.xuenan.itook.core.exception.GlobalException;
import org.xuenan.itook.core.exception.GlobalExceptionStatus;

public class ResponseEntity <T> extends SysModel implements Entity<T>{
    private static final long serialVersionUID = 1L;
    private int code;
    private Boolean success;
    private String message;
    private ExceptionLevel level;
    private String details;
    private long curtime = System.currentTimeMillis();
    private String token;
    private T data;

    public ResponseEntity() {
    }

    public static final <T> ResponseEntity<T> success() {
        return success(null);
    }

    public static final <T> ResponseEntity<T> success(T data) {
        ResponseEntity<T> entity = new ResponseEntity();
        entity.exceptionStatus((ExceptionStatus)GlobalExceptionStatus.OK);
        entity.data = data;
        entity.success = true;
        return entity;
    }

    public static final <T> ResponseEntity<T> fail(ExceptionLevel level, int code, String msg) {
        ResponseEntity<T> entity = new ResponseEntity();
        entity.exceptionStatus(level, code, msg);
        entity.success = false;
        return entity;
    }

    public static final <T> ResponseEntity<T> fail(int code, String msg) {
        return fail(GlobalExceptionStatus.FAIL.getLevel(), code, msg);
    }

    public static final <T> ResponseEntity<T> fail(String msg) {
        return fail(GlobalExceptionStatus.FAIL.getCode(), msg);
    }

    public static final <T> ResponseEntity<T> fail(GlobalException exception) {
        ResponseEntity<T> entity = new ResponseEntity();
        entity.exceptionStatus(exception);
        entity.success = false;
        return entity;
    }

    public static final <T> ResponseEntity<T> fail(ExceptionStatus exceptionStatus) {
        return fail(exceptionStatus.getLevel(), exceptionStatus.getCode(), exceptionStatus.getMsg());
    }

    public static final <T> ResponseEntity<T> fail(Throwable throwable) {
        return fail((new GlobalException(GlobalExceptionStatus.FAIL)).exception(throwable));
    }

    public ResponseEntity<T> exceptionStatus(ExceptionLevel level, int code, String message) {
        this.level = level;
        this.code = code;
        this.message = message;
        return this;
    }

    public ResponseEntity<T> exceptionStatus(GlobalException exception) {
        this.level = exception.getLevel();
        this.code = exception.getCode();
        this.message = exception.getMsg();
        this.details = exception.getDetails();
        return this;
    }

    public ResponseEntity<T> exceptionStatus(ExceptionStatus status) {
        this.level = status.getLevel();
        this.code = status.getCode();
        this.message = status.getMsg();
        return this;
    }

    public ResponseEntity<T> message(String message) {
        this.message = message;
        return this;
    }

    public boolean getSuccess() {
        if (this.success != null) {
            return this.success;
        } else {
            return this.getCode() == 0;
        }
    }

    public T getData() {
        return this.data;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExceptionLevel getLevel() {
        return this.level;
    }

    public long getCurtime() {
        return this.curtime;
    }

    public String getToken() {
        return this.token;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setLevel(ExceptionLevel level) {
        this.level = level;
    }

    public void setCurtime(long curtime) {
        this.curtime = curtime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setData(T data) {
        this.data = data;
    }
}
