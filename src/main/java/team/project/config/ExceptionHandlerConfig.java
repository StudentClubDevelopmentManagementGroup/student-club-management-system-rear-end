package team.project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.base.service.exception.ServiceException;

import java.sql.SQLException;

/* 全局异常捕获处理的配置类 */
@RestControllerAdvice
public class ExceptionHandlerConfig {
    Logger logger = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    /* 处理任意异常 */
    @ExceptionHandler(Exception.class)
    Object ExceptionHandler(Exception exception) {
        logger.error(exception.getMessage());
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }

    /* 处理 controller 层抛出的入参数据不符合约束异常 */
    @ExceptionHandler(BindException.class)
    public Object bindExceptionHandler(BindException bindException) {
        FieldError fieldError = bindException.getFieldError();
        String errMsg = fieldError != null ? fieldError.getDefaultMessage() : bindException.getMessage();

        return new Response<>(ServiceStatus.BAD_REQUEST).statusText("数据不合约束").data(errMsg);
    }

    /* 处理 service 层抛出的异常 */
    @ExceptionHandler(ServiceException.class)
    Object ServiceExceptionHandler(ServiceException serviceException) {
        return new Response<>(serviceException.getStatus()).data(serviceException.getMessage());
    }

    /* 处理 dao 层抛出的 SQL 异常 */
    @ExceptionHandler(SQLException.class)
    Object SQLExceptionHandler(SQLException sqlException) {
        logger.error(sqlException.getMessage());
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }
}
