package org.xuenan.itook.core.consts;

import java.nio.charset.Charset;

public interface Const {
    Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String DEFAULT_LANGUAGE = "en";
    int CHECK_REPEAT_REQUEST_COUNT = 4;
    int CHECK_REPEAT_REQUEST_TIME = 5;
    int REDIS_TRANSACTION_RETRY_NUMBER = 3;
    String USERID_COOKIE_NAME = "USERID_COOKIE_NAME";
    String REQUEST_SERIAL_NUMBER = "REQUEST_SERIAL_NUMBER";
    String REDIRECT_PREFIX = "redirect:";
    String FORWARD_PREFIX = "forward:";
}
