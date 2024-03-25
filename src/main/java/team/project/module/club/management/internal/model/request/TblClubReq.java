package team.project.module.club.management.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/* 封装请求参数，用作接收前端传来的数据 */
@Data
public class TblClubReq {

//    /* 示例 */
//    @NotBlank(message="input_str 字段内容不能为空")  /* jsr303 校验 */
//    private String inputStr;   /* 后端必须要保持小驼峰命名规则 */
    @JsonProperty("departmentId") /* 前端传入的字段名，可能是下划线命名规则 */
    Long     departmentId;
    @JsonProperty("name") /* 前端传入的字段名，可能是下划线命名规则 */
    String   name;
    Integer  pagenum=1;
    Integer  size=20;
}
