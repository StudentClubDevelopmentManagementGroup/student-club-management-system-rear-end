package team.project.module.club.personnelchanges.internal.model.datatransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserMasDTO {
    @JsonProperty("user_id")
    String userId;
}
