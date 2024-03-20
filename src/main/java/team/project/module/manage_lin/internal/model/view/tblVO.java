package team.project.module.manage_lin.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/* VO (View Object) 视图对象
   用于封装“以返回给前端用作数据展示的”响应字段 */
@Data
public class tblVO {

    /* 示例 */
    @JsonProperty("department_id")
    Long     departmentId;
    @JsonProperty("name")
    String   name;
    @JsonProperty("number")
    Integer  number=0;
    String   manager;
    Integer  state;
    Boolean  is_deleted;
    Integer  pagenum=1;
    Integer  size=20;
}
