package org.xuenan.itook.core.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.xuenan.itook.core.Context;
import org.xuenan.itook.core.exception.GlobalException;
import org.xuenan.itook.core.exception.GlobalExceptionStatus;
import org.xuenan.itook.core.init.RequestAfterService;
import org.xuenan.itook.core.init.RequestPostService;
import org.xuenan.itook.core.init.RequestPreService;
import org.xuenan.itook.core.logging.RunTimeLog;
import org.xuenan.itook.core.model.ResponseEntity;
import org.xuenan.itook.core.utils.HttpOutJson;
import org.xuenan.itook.core.utils.MapUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    private Map<String, RequestAfterService> requestAfters;
    private Map<String, RequestPostService> requestPosts;
    private Map<String, RequestPreService> requestPres;
    private ApplicationContext applicationContext;
    private ResponseEntity<?> access_denied;

    public LoggerInterceptor() {
        this.access_denied = ResponseEntity.fail("不允许访问").exceptionStatus(GlobalExceptionStatus.UNAUTHORIZED);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("LoggerInterceptor 开始初始化 .....");
        this.applicationContext = applicationContext;
    }

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        Context.init(request, response);
        String url = Context.getRequestUri();
        logger.debug("请求到达....  {}", url);
        Boolean state = this.handlePre(handler);
        if (state != null) {
            if (state) {
                logger.debug("已接受到请求:{}", url);
                return true;
            }

            logger.info("资源{}不允许访问!", url);
            if (!HttpOutJson.isOutEd()) {
                HttpOutJson.out(this.access_denied);
            }
        }

        Context.remove();
        return false;
    }

    private Boolean handlePre(final Object handler) {
        if (this.requestPres == null) {
            this.requestPres = this.applicationContext.getBeansOfType(RequestPreService.class);
        }

        if (MapUtils.isEmpty(new Map[]{this.requestPres})) {
            return true;
        } else {
            try {
                Iterator var2 = this.requestPres.values().iterator();

                RequestPreService requestPreService;
                do {
                    if (!var2.hasNext()) {
                        return true;
                    }

                    requestPreService = (RequestPreService)var2.next();
                } while(requestPreService.handle(handler));

                return false;
            } catch (GlobalException var4) {
                HttpOutJson.out(ResponseEntity.fail(var4));
            } catch (Exception var5) {
                HttpOutJson.out(ResponseEntity.fail(var5));
                logger.error("权限请求处理前出现位未知异常", var5);
            }

            return null;
        }
    }

    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        if (this.requestPosts == null) {
            this.requestPosts = this.applicationContext.getBeansOfType(RequestPostService.class);
        }

        if (MapUtils.isNotEmpty(new Map[]{this.requestPosts})) {
            this.requestPosts.values().forEach((requestPostService) -> {
                requestPostService.handle(handler, modelAndView);
            });
        }

    }

    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        try {
            if (this.requestAfters == null) {
                this.requestAfters = this.applicationContext.getBeansOfType(RequestAfterService.class);
            }

            if (MapUtils.isNotEmpty(new Map[]{this.requestAfters})) {
                this.requestAfters.values().forEach((requestAfterService) -> {
                    requestAfterService.completion(handler, ex);
                });
            }

            String url = request.getRequestURI();
            long runtime = System.currentTimeMillis() - Context.getBeginTime();
            RunTimeLog.log(runtime, "请求已结束:{}", url);
            if (ex != null) {
                logger.error("请求{}出错 : {}", url, ex.getMessage());
            }
        } finally {
            Context.remove();
        }

    }
}
