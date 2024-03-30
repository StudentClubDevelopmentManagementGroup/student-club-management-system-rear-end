package team.project.base.exceptionhandler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;

/* 处理 Sa-Token 权限认证相关的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.saTokenExceptionHandler)
public class SaTokenExceptionHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /* 未登录 */
    @ExceptionHandler(NotLoginException.class)
    Object handleNotLoginException(NotLoginException exception) {
        return new Response<>(ServiceStatus.UNAUTHORIZED).statusText("未登录");
    }

    /* 角色验证失败 */
    @ExceptionHandler(NotRoleException.class)
    Object handleNotRoleException(NotRoleException exception) {
        return new Response<>(ServiceStatus.UNAUTHORIZED).statusText("用户不拥有指定角色，无权执行请求");
    }

    /* 权限验证失败 */
    @ExceptionHandler(NotPermissionException.class)
    Object handleNotPermissionException(NotPermissionException exception) {
        return new Response<>(ServiceStatus.UNAUTHORIZED).statusText("用户未拥有指定权限，无权执行请求");
    }
}
