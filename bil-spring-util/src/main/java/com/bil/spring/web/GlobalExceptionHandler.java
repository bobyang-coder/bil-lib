package com.bil.spring.web;

import com.bil.spring.model.ResponseResult;
import com.bil.spring.model.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author haibo.yang
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult defaultHandler(HttpServletRequest request, Exception exception) {
        StringBuffer url = request.getRequestURL();
        String method = request.getMethod();
        log.error("Exception url:{}, method:{}", url, method, exception);
        if (exception instanceof MethodArgumentNotValidException
                || exception instanceof HttpMessageNotReadableException
                || exception instanceof HttpRequestMethodNotSupportedException
                || exception instanceof ServletRequestBindingException
                || exception instanceof HttpMessageNotWritableException
        ) {
            return ResponseResult.paramError(getParamErrorDetails(exception));
        } else if (exception instanceof IllegalArgumentException) {
            return ResponseResult.paramError(exception.getMessage());
        } else {
            exception.printStackTrace();
            return ResponseResult.fail(ResultCode.SYSTEM_ERROR);
        }
    }

    private String getParamErrorDetails(Exception e) {
        StringBuilder sb = new StringBuilder();
        if (e instanceof MethodArgumentNotValidException) {
            Iterator var2 = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().iterator();
            while (var2.hasNext()) {
                ObjectError error = (ObjectError) var2.next();
                DefaultMessageSourceResolvable messageSourceResolvable = (DefaultMessageSourceResolvable) Objects.requireNonNull(error.getArguments())[0];
                sb.append("[key=").append(messageSourceResolvable.getCode()).append(",");
                sb.append(error.getDefaultMessage()).append("] ");
            }
        }
        if (e instanceof MissingServletRequestParameterException) {
            sb.append("[key=");
            sb.append(((MissingServletRequestParameterException) e).getParameterName());
            sb.append("],");
            sb.append(e.getMessage());
        }
        if (e instanceof HttpMessageNotReadableException) {
            sb.append(e.getLocalizedMessage());
        }
        if (StringUtils.isEmpty(sb.toString())) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }
}
