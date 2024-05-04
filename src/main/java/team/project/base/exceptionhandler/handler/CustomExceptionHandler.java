package team.project.base.exceptionhandler.handler;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.exception.InvalidParamException;
import team.project.base.controller.response.Response;
import team.project.base.exceptionhandler.order.ExceptionHandlerOrder;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

/* 处理自定义的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.customExceptionHandler)
public class CustomExceptionHandler {

    @ExceptionHandler(InvalidParamException.class)
    Object handle(InvalidParamException exception) {
        return new Response<>(ServiceStatus.BAD_REQUEST).data(exception.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    Object handle(ServiceException exception) {
        if (exception.getStatus().is5XX()){
            return new Response<>(exception.getStatus());
        } else {
            return new Response<>(exception.getStatus()).data(exception.getMessage());
        }
    }
}
