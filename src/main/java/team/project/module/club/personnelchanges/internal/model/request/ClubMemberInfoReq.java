package team.project.module.club.personnelchanges.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class ClubMemberInfoReq {

    @NotNull(message="未指定社团id")
    @ClubIdConstraint
    @JsonProperty("club_id")
    Long     clubId;

    @JsonProperty("department_id")
    Long     departmentId; /* <- 成员所属院系，而非社团 */

    @JsonProperty("name")
    String   name; /* <- 成员名字，而非社团 */

    @Min(value = 1, message="页码不合法")
    @JsonProperty("page_num")
    Integer  pageNum=1;

    @Min(value = 1, message="每页大小不合法")
    @JsonProperty("page_size")
    Integer  size=20;
}
