package team.project.base.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;

import java.sql.SQLException;

/* 处理 mybatis-plus 抛出的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.mybatisPlusExceptionHandler)
public class MybatisPlusExceptionHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /* 执行 SQL 出错 */
    @ExceptionHandler(SQLException.class)
    Object handle(SQLException sqlException) {
        logger.error("执行 SQL 出错\n" + sqlException.getMessage());
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }

    /* “唯一键”约束 */
    @ExceptionHandler(DuplicateKeyException.class)
    Object handle(DuplicateKeyException exception) {
        logger.error("执行 SQL 出错，违反“唯一键”约束\n" + exception.getMessage());
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }

    /* “完整性”约束 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    Object handle(DataIntegrityViolationException exception) {
        logger.error("执行 SQL 出错，违反“完整性”约束\n" + exception.getMessage());
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }
}
