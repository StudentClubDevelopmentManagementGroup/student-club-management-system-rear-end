package team.project.base.exceptionhandler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.Response;
import team.project.base.service.status.ServiceStatus;

/* 处理 Sa-Token 权限认证相关的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.saTokenExceptionHandler)
public class SaTokenExceptionHandler {

    /* 未登录 */
    @ExceptionHandler(NotLoginException.class)
    Object handle(NotLoginException exception) {
        String message = switch (exception.getType()) {
            case NotLoginException.NOT_TOKEN     -> "未能读取到有效 token";
            case NotLoginException.INVALID_TOKEN -> "token 无效";
            case NotLoginException.TOKEN_TIMEOUT -> "token 已过期";
            case NotLoginException.BE_REPLACED   -> "token 已被顶下线";
            case NotLoginException.TOKEN_FREEZE  -> "token 已被冻结";
            case NotLoginException.NO_PREFIX     -> "未按照指定前缀提交 token";
            default                              -> "当前会话未登录";
        };

        return new Response<>(ServiceStatus.UNAUTHORIZED).data(message + "，请重新登录");
    }

    /* 角色验证失败 */
    @ExceptionHandler(NotRoleException.class)
    Object handle(NotRoleException exception) {
        return new Response<>(ServiceStatus.UNAUTHORIZED).data("用户不拥有指定角色，无权执行请求");
    }

    /* 权限验证失败 */
    @ExceptionHandler(NotPermissionException.class)
    Object handle(NotPermissionException exception) {
        return new Response<>(ServiceStatus.UNAUTHORIZED).data("用户未拥有指定权限，无权执行请求");
    }
}
