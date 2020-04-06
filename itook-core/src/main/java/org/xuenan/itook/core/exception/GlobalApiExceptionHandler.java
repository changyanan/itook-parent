package org.xuenan.itook.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.xuenan.itook.core.Context;
import org.xuenan.itook.core.model.ResponseEntity;
import org.xuenan.itook.core.utils.HttpOutJson;
import org.xuenan.itook.core.utils.JSONUtils;
import org.xuenan.itook.core.utils.ListUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger("xuenan.log");

    public GlobalApiExceptionHandler() {
    }

    @ExceptionHandler({GlobalException.class})
    public ModelAndView handleControllerException(HttpServletRequest request, HttpServletResponse response, GlobalException ex) {
        ResponseEntity<?> responseEntity = ResponseEntity.fail(ex);
        return outException(request, response, responseEntity, ex);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ModelAndView handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<?> responseEntity = ResponseEntity.fail(new GlobalException(GlobalExceptionStatus.REQUEST_INVALID_PARAMETER));
        responseEntity.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
        responseEntity.setDetails(this.geJson(ex.getBindingResult()));
        HttpOutJson.out(responseEntity, HttpStatus.BAD_REQUEST.value());
        return null;
    }

    @ExceptionHandler({BindException.class})
    public ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<?> responseEntity = ResponseEntity.fail(new GlobalException(GlobalExceptionStatus.REQUEST_INVALID_PARAMETER));
        responseEntity.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
        responseEntity.setDetails(this.geJson(ex.getBindingResult()));
        HttpOutJson.out(responseEntity, HttpStatus.BAD_REQUEST.value());
        return null;
    }

    private String geJson(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> errors = new HashMap(fieldErrors.size());
        fieldErrors.forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return JSONUtils.toJSONString(errors);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ModelAndView handleBindException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<?> responseEntity = ResponseEntity.fail(new GlobalException(GlobalExceptionStatus.REQUEST_INVALID_PARAMETER));
        responseEntity.setMessage(String.format("参数%s必须输入", ex.getParameterName()));
        HttpOutJson.out(responseEntity, HttpStatus.BAD_REQUEST.value());
        return null;
    }

    @ExceptionHandler({Throwable.class})
    public ModelAndView ControllerException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        return outException(request, response, ex);
    }

    public static ModelAndView outException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        ResponseEntity<?> responseEntity = ResponseEntity.fail(ex);
        return outException(request, response, responseEntity, ex);
    }

    public static ModelAndView outException(HttpServletRequest request, HttpServletResponse response, ResponseEntity<?> responseEntity, Throwable ex) {
        HttpStatus status = getHttpStatus(ex);
        if (status != HttpStatus.INTERNAL_SERVER_ERROR && status != HttpStatus.OK) {
            responseEntity.setLevel(ExceptionLevel.INFO);
        }

        logLevelOut(responseEntity, ex);
        HttpOutJson.out(responseEntity, status.value());
        return null;
    }

    private static void logLevelOut(ResponseEntity<?> responseEntity, Throwable ex) {
        ListUtils<Object> lu = ListUtils.n().a(responseEntity.getLevel()).a(responseEntity.getCode()).a(responseEntity.getMessage()).a(Context.getContext());
        String format = "\n    >>{}.{}<< 系统出现了异常 ********* (( {} ))*********\n    >>>>>> {}";
        switch(responseEntity.getLevel()) {
            case OFF:
                return;
            case ERROR:
                logger.error(format, lu.a(ex).to().toArray());
                break;
            case WARN:
                logger.warn(format, lu.a(ex).to().toArray());
                break;
            case INFO:
                logger.info(format, lu.to().toArray());
                break;
            case DEBUG:
                logger.info(format, lu.to().toArray());
                break;
            case TRACE:
                logger.trace(format, lu.to().toArray());
        }

    }

    public static final HttpStatus getHttpStatus(Throwable ex) {
        HttpStatus status = null;
        if (ex != null && !(ex instanceof GlobalException)) {
            if (ex instanceof HttpRequestMethodNotSupportedException) {
                status = HttpStatus.METHOD_NOT_ALLOWED;
            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                status = HttpStatus.NOT_ACCEPTABLE;
            } else if (ex instanceof MissingPathVariableException) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            } else if (ex instanceof MissingServletRequestParameterException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof ServletRequestBindingException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof ConversionNotSupportedException) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            } else if (ex instanceof TypeMismatchException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof HttpMessageNotReadableException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof HttpMessageNotWritableException) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            } else if (ex instanceof MethodArgumentNotValidException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof MissingServletRequestPartException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof BindException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof NoHandlerFoundException) {
                status = HttpStatus.NOT_FOUND;
            } else if (ex instanceof AsyncRequestTimeoutException) {
                status = HttpStatus.SERVICE_UNAVAILABLE;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            status = HttpStatus.OK;
        }

        return status;
    }
}
