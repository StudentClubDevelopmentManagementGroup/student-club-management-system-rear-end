package team.project.module.club.management.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class selectClubVO {
    @JsonProperty("id")
    Long      id;
    @JsonProperty("is_deleted")
    Boolean   isDeleted;
    @JsonProperty("update_time")
    Timestamp updateTime;
    @JsonProperty("department_id")
    Long      departmentId;
    @JsonProperty("department_name")
    String    departmentName;
    @JsonProperty("name")
    String    name;
    @JsonProperty("state")
    Integer   state;
    @JsonProperty("introduction")
    String    introduction;
}
