package team.project.module.club.management.internal.model.datatransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ClubMasDTO {
    @JsonProperty("name")
    String   name;
    @JsonProperty("department_name")
    String   departmentName;
    @JsonProperty("department_id")
    Long     departmentId;
    @JsonProperty("club_id")
    Long     clubId;
    @JsonProperty("number")
    Integer  number=0;
    @JsonProperty("state")
    Boolean  state;
    @JsonProperty("is_deleted")
    Boolean  deleted;
    @JsonProperty("manager")
    String   manager;
}
