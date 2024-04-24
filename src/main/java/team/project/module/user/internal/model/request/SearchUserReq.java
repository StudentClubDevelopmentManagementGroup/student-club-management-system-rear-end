package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchUserReq {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_name")
    private String userName;
}
