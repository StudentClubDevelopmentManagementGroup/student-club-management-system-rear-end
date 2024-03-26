package team.project.module.club.management.internal.model.view;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ClubMasVO {

    @JsonProperty("name")
    String   name;
    @JsonProperty("department_name")
    String   departmentName;
    @JsonProperty("department_id")
    Long     departmentId;
    @JsonProperty("number")
    Integer  number=0;
    @JsonProperty("state")
    Boolean  state;
    @JsonProperty("is_deleted")
    Boolean  deleted;
    @JsonProperty("manager")
    String   manager;
    @JsonProperty("pagenum")
    Integer  pageNum =1;
    @JsonProperty("size")
    Integer  size=20;
}
