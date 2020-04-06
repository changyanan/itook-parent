package org.xuenan.itook.core.init;

import org.springframework.web.servlet.ModelAndView;

@FunctionalInterface
public interface RequestPostService {
    void handle(Object handler, ModelAndView modelAndView);
}
