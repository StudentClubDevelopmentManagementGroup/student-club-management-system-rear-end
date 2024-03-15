package team.project.base.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.exception.ServiceException;

/* 处理自定义的 service 异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.ServiceExceptionHandler)
public class ServiceExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    /* 处理 service 层抛出的自定义异常 */
    @ExceptionHandler(ServiceException.class)
    Object handleServiceException(ServiceException exception) {
        return new Response<>(exception.getStatus()).data(exception.getMessage());
    }
}
