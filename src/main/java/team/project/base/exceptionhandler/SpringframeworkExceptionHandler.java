package team.project.base.exceptionhandler;

import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;


/* 处理 springframework 抛出的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.springframeworkExceptionHandler)
public class SpringframeworkExceptionHandler {

    /* http 请求缺少必要的参数 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handle(MissingServletRequestParameterException exception) {
        return new Response<>(ServiceStatus.BAD_REQUEST).data("缺少必要的请求参数：" + exception.getParameterName());
    }

    /* http 请求缺少必要的部分 */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Object handle(MissingServletRequestPartException exception) {
        return new Response<>(ServiceStatus.BAD_REQUEST).data("缺少必要的请求部分：" + exception.getRequestPartName());
    }

    /* http 请求中的数据绑定到 controller 方法的参数对象时绑定失败 */
    @ExceptionHandler(BindException.class)
    public Object handle(BindException exception) {
        FieldError fieldError = exception.getFieldError();
        String errMsg = fieldError != null ? fieldError.getDefaultMessage() : exception.getMessage();
        return new Response<>(ServiceStatus.BAD_REQUEST).data("入参不合约束：" + errMsg);
    }

    /* 参数校验 */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Object handle(HandlerMethodValidationException exception) {

        ParameterValidationResult validationResult = exception.getAllValidationResults().get(0);
        String paramName = validationResult.getMethodParameter().getParameterName();
        String defaultMessage = validationResult.getResolvableErrors().get(0).getDefaultMessage();
        String errorMessage = "参数校验未通过：" + defaultMessage;

        if (exception.getStatusCode().is4xxClientError()) {
            return new Response<>(ServiceStatus.BAD_REQUEST).data(errorMessage);
        }
        if (exception.getStatusCode().is5xxServerError()) {
            return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR).data(errorMessage);
        }

        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }

    /* 参数转换失败 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handle(MethodArgumentTypeMismatchException exception) {
        return new Response<>(ServiceStatus.BAD_REQUEST).data("请求参数的类型与所需的类型不匹配");
    }

    /* 无法解析 http 请求 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handle(HttpMessageNotReadableException exception) {
        /* 可能是：
          - 数据类型不匹配，无法转换
          - 期望接收非空数据，但请求的内容为空
        */
        return new Response<>(ServiceStatus.BAD_REQUEST).data("请求的内容不符合预期的格式或结构，无法进行解析");
    }

    /* 访问不存在的资源（例如一个错误URL地址） */
    @ExceptionHandler(NoResourceFoundException.class)
    public Object handle(NoResourceFoundException exception) {
        return new Response<>(ServiceStatus.NOT_FOUND).data("错误的 URL 地址，访问了不存在的资源");
    }

    /* 上传文件大小超出限制 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Object handle(MaxUploadSizeExceededException exception) {
        return new Response<>(ServiceStatus.PAYLOAD_TOO_LARGE).data("上传文件大小超过限制");
    }
}
