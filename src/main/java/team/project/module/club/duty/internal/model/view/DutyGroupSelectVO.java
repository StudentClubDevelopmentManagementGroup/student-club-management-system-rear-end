package team.project.module.club.duty.internal.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DutyGroupSelectVO {
    @JsonProperty("id")
    Long      id;
    @JsonProperty("club_id")
    Long      clubId;
    @JsonProperty("member_id")
    String    memberId;
    @JsonProperty("group_name")
    String    name;
    @JsonProperty("user_name")
    String    userName="test";
}
