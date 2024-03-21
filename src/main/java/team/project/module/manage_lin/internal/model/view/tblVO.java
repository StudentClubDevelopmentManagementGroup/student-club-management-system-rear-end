package team.project.module.manage_lin.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/* VO (View Object) 视图对象
   用于封装“以返回给前端用作数据展示的”响应字段 */
@Data
public class tblVO {

    @JsonProperty("name")
    String   name;
    @JsonProperty("department_name")
    String   department_name;
    @JsonProperty("number")
    Integer  number=0;
    @JsonProperty("state")
    Integer  state;
    @JsonProperty("is_deleted")
    Boolean  is_deleted;
    @JsonProperty("manager")
    String   manager;
    @JsonProperty("pagenum")
    Integer  pagenum=1;
    @JsonProperty("size")
    Integer  size=20;
}
