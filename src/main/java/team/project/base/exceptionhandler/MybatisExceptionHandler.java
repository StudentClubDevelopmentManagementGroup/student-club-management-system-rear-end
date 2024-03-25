package team.project.base.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;

import java.sql.SQLException;

/* 处理 mybatis-plus 抛出的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.mybatisExceptionHandler)
public class MybatisExceptionHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /* 执行 SQL 出错 */
    @ExceptionHandler(SQLException.class)
    Object handleSQLException(SQLException sqlException) {
        logger.error("执行 SQL 出错\n" + sqlException.getMessage());
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }
}
