package team.project.module.club.personnelchanges.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class ClubMemberInfoReq {

    @ClubIdConstraint
    @JsonProperty("club_id")
    Long     clubId;

    @JsonProperty("department_id")
    Long     departmentId; /* <- 成员所属院系，而非社团 */

    @JsonProperty("name")
    String   name; /* <- 成员名字，而非社团 */

    @Min(value = 1, message="页码不合法")
    Integer  pagenum=1;

    @Min(value = 1, message="每页大小不合法")
    Integer  size=20;
}
