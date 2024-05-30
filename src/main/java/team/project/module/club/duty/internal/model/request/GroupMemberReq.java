package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import team.project.module.club.management.export.model.annotation.ClubIdConstraint;

@Data
public class GroupMemberReq {
    @NotBlank
    @JsonProperty("group_name")
    String name;

    @NotBlank
    @JsonProperty("member_id")
    String memberId;

    @NotNull
    @ClubIdConstraint
    @JsonProperty("club_id")
    Long   clubId;
}
