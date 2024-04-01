package team.project.base.exceptionhandler;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;

/* 处理一般的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.generalExceptionHandler)
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    Object handle(Exception exception) {
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }
}
