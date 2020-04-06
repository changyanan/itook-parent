package org.xuenan.itook.core.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ExceptionController {
    public ExceptionController() {
    }

    @GetMapping({"/exception/500"})
    public ModelAndView serverError(HttpServletRequest request, HttpServletResponse response) {
        Exception ex = (Exception)request.getAttribute("javax.servlet.error.exception");
        return GlobalApiExceptionHandler.outException(request, response, ex);
    }
}
