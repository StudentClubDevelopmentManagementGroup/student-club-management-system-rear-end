package team.project.base.service.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/* 自定义的服务端响应状态码（虽然和 http 的状态码一致，但
   注意，这个是 ServiceStatus，而不是 HttpStatus） */
@AllArgsConstructor
@Getter
@ToString
public enum ServiceStatus {
    /* 成功响应 */
    SUCCESS              (200, "执行成功"), /* 请求所希望数据将随此响应返回 */
    CREATED              (201, "创建成功"), /* 请求成功，并创建了新的资源 */
    /* 客户端异常 */
    BAD_REQUEST          (400, "无效请求"), /* 客户端错误（请求语法错误、无效的请求），服务器无法理解 */
    UNAUTHORIZED         (401, "身份验证未通过"), /* 当前请求需要用户验证 */
    FORBIDDEN            (403, "没有权限"), /* 服务器理解请求，但客户端没有访问内容的权限，服务器拒绝执行请求 */
    NOT_FOUND            (404, "找不到资源"), /* 在服务器上找不到请求的资源 */
    REQUEST_TIMEOUT      (408, "超时"), /* 服务器等待请求时间过长 */
    CONFLICT             (409, "冲突"), /* 请求与服务器的当前状态冲突 */
    PAYLOAD_TOO_LARGE    (413, "请求体过大"), /* 上传的文件太大 */
    UNPROCESSABLE_ENTITY (422, "无法处理"), /* 请求格式正确，但语义错误 */
    FAILED_DEPENDENCY    (424, "前请求失败"), /* 由于前一个请求失败，本次请求失败 */
    /* 服务的异常 */
    INTERNAL_SERVER_ERROR(500, "服务器内部异常"), /* 服务器遇到了不知道如何处理的情况 */
    NOT_IMPLEMENTED      (501, "没有实现该功能"), /* 请求了未实现的功能 */

    ;
    private final int    statusCode;
    private final String statusText;
}
