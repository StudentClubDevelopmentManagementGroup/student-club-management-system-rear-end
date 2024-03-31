package team.project.module.club.management.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ClubVO {
    @JsonProperty("name")
    String   name;
    @JsonProperty("department_id")
    Long     departmentId;
}
