package team.project.base.controller.queryparam;

import lombok.Getter;

/**
 * <p>自定义异常
 * <p>在解析查询参数装填对象时，如果解析失败则抛出此异常
 * */
@Getter
public class QueryParameterResolutionFailureException extends RuntimeException {
    public QueryParameterResolutionFailureException(String message) {
        super(message);
    }
}