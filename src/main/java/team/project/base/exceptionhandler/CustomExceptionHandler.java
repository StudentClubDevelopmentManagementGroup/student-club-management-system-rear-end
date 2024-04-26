package team.project.base.exceptionhandler;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.controller.queryparam.QueryParameterResolutionFailureException;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

/* 处理自定义的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.customExceptionHandler)
public class CustomExceptionHandler {

    /* controller 层的异常 */
    @ExceptionHandler(QueryParameterResolutionFailureException.class)
    Object handle(QueryParameterResolutionFailureException exception) {
        return new Response<>(ServiceStatus.BAD_REQUEST).data(exception.getMessage());
    }

    /* service 层的异常 */
    @ExceptionHandler(ServiceException.class)
    Object handle(ServiceException exception) {
        return new Response<>(exception.getStatus()).data(exception.getMessage());
    }
}
