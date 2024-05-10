package team.project.module.club.duty.internal.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupMemberReq {
    @NotBlank
    String name;

    @NotBlank
    String memberId;

    @NotBlank
    Long clubId;
}
