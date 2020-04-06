package org.xuenan.itook.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.Context;
import org.xuenan.itook.core.init.LoginInitalize;
import org.xuenan.itook.core.init.TokenOutService;
import org.xuenan.itook.core.model.ResponseEntity;
import org.xuenan.itook.core.model.SysUser;
@Component
@ComponentScan
public class ContextUtils {
    private static final Logger logger = LoggerFactory.getLogger(ContextUtils.class);
    private static LoginInitalize loginInitalize;
    private static TokenOutService tokenOutService;

    public ContextUtils() {
    }

    @Autowired(
            required = false
    )
    void set(LoginInitalize loginInitalize) {
        ContextUtils.loginInitalize = loginInitalize;
    }

    @Autowired(
            required = false
    )
    public void setTokenOutService(TokenOutService tokenOutService) {
        ContextUtils.tokenOutService = tokenOutService;
    }

    public static SysUser initLoginInfo() {
        logger.debug("初始化用户登录信息");
        if (loginInitalize == null) {
            return null;
        } else {
            SysUser user = loginInitalize.findLoginUser();
            Context.setUser(user);
            return user;
        }
    }

    public static void setToken(ResponseEntity<?> responseEntity) {
        if (tokenOutService != null) {
            tokenOutService.setToken(responseEntity);
        }

    }
}
