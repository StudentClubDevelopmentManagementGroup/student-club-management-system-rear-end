package team.project.base.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;

/* 处理一般的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.GeneralExceptionHandler)
public class GeneralExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    /* 处理任意异常 */
    @ExceptionHandler(Exception.class)
    Object handleExceptionHandler(Exception exception) {
        logger.error(
            "异常类：" + exception.getClass() + "\n" +
            "信息：" + exception.getMessage() +
            "  （该异常由通用异常处理器捕获，请考虑是否为其配备专门的异常处理器）\n"
        );
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }
}
