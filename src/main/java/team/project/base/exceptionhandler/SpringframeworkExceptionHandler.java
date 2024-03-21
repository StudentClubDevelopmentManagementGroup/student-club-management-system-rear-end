package team.project.base.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;

/* 处理 springframework 抛出的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.springframeworkExceptionHandler)
public class SpringframeworkExceptionHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    Logger loggerForDebug = LoggerFactory.getLogger(this.getClass());

    /* 上传文件大小超出限制 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Object handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return new Response<>(ServiceStatus.PAYLOAD_TOO_LARGE).statusText("上传文件大小超过限制");
    }

    /* 处理 controller 层抛出的缺少必要的请求参数异常 [ begin ] */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Object handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        return new Response<>(ServiceStatus.BAD_REQUEST).statusText("缺少必要的请求参数").data(exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException exception) {
        FieldError fieldError = exception.getFieldError();
        String errMsg = fieldError != null ? fieldError.getDefaultMessage() : exception.getMessage();

        return new Response<>(ServiceStatus.BAD_REQUEST).statusText("url入参数据不合约束").data(errMsg);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public Object handleHandlerMethodValidationException(HandlerMethodValidationException exception) {
        logger.error(exception.getMessage());
        return new Response<>(ServiceStatus.BAD_REQUEST).statusText("url入参数据不合约束").data(exception.getMessage());
    }
    /* 处理 controller 层抛出的缺少必要的请求参数异常 [ end ] */

    /*  */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        /* 可能是：
          - 数据类型不匹配，无法转换
          - 期望接收非空数据，但请求的内容为空
        */
        loggerForDebug.warn("无法进行解析请求：" + exception.getMessage());
        return new Response<>(ServiceStatus.BAD_REQUEST).data("请求的内容不符合预期的格式或结构，无法进行解析");
    }

    /* 访问不存在的资源（例如一个错误URL地址） */
    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException(NoResourceFoundException exception) {
        loggerForDebug.warn("访问了不存在的资源：" + exception.getResourcePath());
        return new Response<>(ServiceStatus.NOT_FOUND);
    }
}
