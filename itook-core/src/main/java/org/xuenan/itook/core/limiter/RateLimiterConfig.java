package org.xuenan.itook.core.limiter;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.xuenan.itook.core.exception.GlobalException;
import org.xuenan.itook.core.exception.GlobalExceptionStatus;
import org.xuenan.itook.core.init.ControllerPre;
import org.xuenan.itook.core.web.config.WebProperties;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableConfigurationProperties({WebProperties.class})
public class RateLimiterConfig {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterConfig.class);
    private final Map<Method, RateLimiter> map = new HashMap();

    public RateLimiterConfig() {
    }

    private RateLimiter getRateLimiter(Method method) {
        if (this.map.containsKey(method)) {
            return (RateLimiter)this.map.get(method);
        } else {
            org.xuenan.itook.core.limiter.RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, org.xuenan.itook.core.limiter.RateLimiter.class);
            if (rateLimiter == null) {
                return null;
            } else {
                RateLimiter limiter = RateLimiter.create(rateLimiter.value());
                RateLimiter reLimiter = (RateLimiter)this.map.putIfAbsent(method, limiter);
                return reLimiter != null ? reLimiter : limiter;
            }
        }
    }

    @Bean
    public ControllerPre limiter(WebProperties webProperties) {
        return (point, method) -> {
            if (!webProperties.getLimiter().isEnabled()) {
                log.info("未开启流控限制");
            } else {
                RateLimiter limiter = this.getRateLimiter(method);
                if (limiter != null && !limiter.tryAcquire()) {
                    log.warn("{}.{} 触发流量控制", method.getDeclaringClass(), method.getName());
                    GlobalException.exception(GlobalExceptionStatus.FLOW_CONTROL);
                }

            }
        };
    }
}
