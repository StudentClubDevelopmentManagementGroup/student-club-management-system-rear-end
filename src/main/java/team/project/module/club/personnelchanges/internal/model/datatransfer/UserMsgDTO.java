package team.project.module.club.personnelchanges.internal.model.datatransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserMsgDTO {
    @JsonProperty("user_id")
    String userId;
}
