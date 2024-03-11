package team.project.module._template.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/* VO (View Object) 视图对象
   用于封装“以返回给前端用作数据展示的”响应字段 */
@Data
public class TmplVO {

    /* 示例 */
    @JsonProperty("output_str") /* 返回给前端的字段名，可能是下划线命名规则 */
    private String outputStr;   /* 后端要保持小驼峰命名规则 */
}
