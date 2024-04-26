package team.project.base.controller.queryparam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** TODO 待补全说明
 * 以支持使用对象来接收查询参数，能使用 @JsonProperty 起别名，使用 jsr303 参数校验，不能嵌套对象
 * 查询参数（Query parameters）指的是出现在 URL 中 ”?“ 后面的键值对参数
 * */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {
}
