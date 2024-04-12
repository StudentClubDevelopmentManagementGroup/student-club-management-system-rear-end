package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import team.project.module.user.export.model.annotation.UserPasswordConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Data
public class UserIdAndPasswordReq {

    @UserIdConstraint
    @JsonProperty("user_id")
    private String userId;

    @UserPasswordConstraint
    @JsonProperty("pwd")
    private String password;
}
