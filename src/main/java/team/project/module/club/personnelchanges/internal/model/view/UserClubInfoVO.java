package team.project.module.club.personnelchanges.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserClubInfoVO {
    @JsonProperty("club_name")
    String clubName;
    @JsonProperty("club_id")
    Long   clubId;
    @JsonProperty("department_name")
    String departmentName;
    @JsonProperty("role")
    String role;
}
