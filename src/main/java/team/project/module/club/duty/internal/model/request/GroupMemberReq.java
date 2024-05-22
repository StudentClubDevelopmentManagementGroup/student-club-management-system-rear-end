package team.project.module.club.duty.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupMemberReq {
    @NotBlank
    @JsonProperty("group_name")
    String name;

    @NotBlank
    @JsonProperty("member_id")
    String memberId;

    @NotBlank
    @JsonProperty("club_id")
    Long   clubId;
}
