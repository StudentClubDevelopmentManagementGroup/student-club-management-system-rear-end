package team.project.base.service.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/* 自定义的服务端响应状态码（虽然和 http 的状态码一致，但也算是自定义的状态码） */
@AllArgsConstructor
@Getter
@ToString
public enum ServiceStatus {
    /* 成功响应 */
    SUCCESS                 (200, "执行成功"),
    CREATED                 (201, "创建成功"),

    /* 客户端异常 */
    BAD_REQUEST             (400, "无效请求"),
    UNAUTHORIZED            (401, "身份验证失败"),
    FORBIDDEN               (403, "没有权限"),
    NOT_FOUND               (404, "找不到资源"),
    REQUEST_TIMEOUT         (408, "超时"),
    CONFLICT                (409, "冲突"),
    UNPROCESSABLE_ENTITY    (422, "无法处理"),

    /* 服务的异常 */
    INTERNAL_SERVER_ERROR   (500, "服务器内部异常");

    private final int    statusCode;
    private final String statusText;
}
