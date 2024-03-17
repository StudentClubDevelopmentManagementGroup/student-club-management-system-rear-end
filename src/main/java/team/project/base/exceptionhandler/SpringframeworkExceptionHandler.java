package team.project.base.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
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
@Order(ExceptionHandlerOrder.SpringframeworkExceptionHandler)
public class SpringframeworkExceptionHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /* 处理上传文件大小超出限制的异常 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Object handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return new Response<>(ServiceStatus.PAYLOAD_TOO_LARGE).statusText("文件大小超过限制");
    }

    /* 处理 controller 层抛出的缺少必要的请求参数异常 */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Object handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        return new Response<>(ServiceStatus.BAD_REQUEST).statusText("缺少必要的请求参数").data(exception.getMessage());
    }

    /* 处理 controller 层抛出的入参数据不符合约束异常 ? */
    /* 存疑：BindException 和 HandlerMethodValidationException 都是检查入参合法性？？ */
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException exception) {
        FieldError fieldError = exception.getFieldError();
        String errMsg = fieldError != null ? fieldError.getDefaultMessage() : exception.getMessage();

        return new Response<>(ServiceStatus.BAD_REQUEST).statusText("url入参数据不合约束").data(errMsg);
    }

    /* 处理 controller 层抛出的入参数据不符合约束异常 ? */
    /* 存疑：BindException 和 HandlerMethodValidationException 都是检查入参合法性？？ */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Object handleHandlerMethodValidationException(HandlerMethodValidationException exception) {
        logger.error(exception.getMessage());
        return new Response<>(ServiceStatus.BAD_REQUEST).data("url入参数据不合约束");
    }

    /* 处理访问不存在的资源（例如一个错误URL地址）时抛出的异常 */
    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException(NoResourceFoundException exception){
        return new Response<>(ServiceStatus.NOT_FOUND);
    }
}
