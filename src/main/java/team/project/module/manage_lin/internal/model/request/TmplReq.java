package team.project.module.manage_lin.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/* 封装请求参数，用作接收前端传来的数据 */
@Data
public class TmplReq {

    /* 示例 */
    @NotBlank(message="input_str 字段内容不能为空")  /* jsr303 校验 */
    @JsonProperty("input_str") /* 前端传入的字段名，可能是下划线命名规则 */
    private String inputStr;   /* 后端必须要保持小驼峰命名规则 */

}
