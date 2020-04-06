package org.xuenan.itook.core.exception;

public enum  GlobalExceptionStatus implements ExceptionStatus{
    OK(ExceptionLevel.DEBUG, 0, "success"),
    FAIL(ExceptionLevel.ERROR, 1, "操作失败"),
    UNAUTHORIZED(ExceptionLevel.ERROR, 2, "未认证（签名错误）"),
    NOT_FOUND(ExceptionLevel.WARN, 3, "接口不存在"),
    FTP_ERROR(ExceptionLevel.WARN, 4, "ftp错误"),
    INVALID_PARAMETER(ExceptionLevel.INFO, 5, "参数校验失败"),
    REQUEST_INVALID_PARAMETER(ExceptionLevel.INFO, 6, "请求参数校验失败"),
    SERVER_ERROR(ExceptionLevel.ERROR, 7, "服务器错误"),
    DEBUG(ExceptionLevel.DEBUG, 8, "调试"),
    INFO(ExceptionLevel.INFO, 9, "提示"),
    WARN(ExceptionLevel.WARN, 11, "警告"),
    SIGNATURE(ExceptionLevel.ERROR, 10, "签名认证失败"),
    FLOW_CONTROL(ExceptionLevel.INFO, 12, "触发流量限制"),
    GATEWAY_ERROR(ExceptionLevel.ERROR, 502, "网关错误");

    private static final int mark = 9;
    private final int code;
    private final ExceptionLevel level;
    private final String msg;

    private GlobalExceptionStatus(int code, String msg) {
        this(ExceptionLevel.INFO, code, msg);
    }

    private GlobalExceptionStatus(ExceptionLevel level, int code, String msg) {
        this.msg = msg;
        this.code = code == 0 ? 0 : 900000000 + code;
        this.level = level;
    }

    public int getCode() {
        return this.code;
    }

    public ExceptionLevel getLevel() {
        return this.level;
    }

    public String getMsg() {
        return this.msg;
    }
}
