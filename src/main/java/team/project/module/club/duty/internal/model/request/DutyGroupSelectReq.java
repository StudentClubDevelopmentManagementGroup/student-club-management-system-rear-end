package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class DutyGroupSelectReq {
    @NotNull
    @ClubIdConstraint
    @JsonProperty("club_id")
    Long      clubId;

    @JsonProperty("group_name")
    String    groupName;

    @JsonProperty("name")
    String    name;

    @Min(value = 1, message="页码不合法")
    @JsonProperty("page_num")
    Integer   pageNum = 1;

    @Min(value = 1, message="每页大小不合法")
    Integer   size = 20;
}
