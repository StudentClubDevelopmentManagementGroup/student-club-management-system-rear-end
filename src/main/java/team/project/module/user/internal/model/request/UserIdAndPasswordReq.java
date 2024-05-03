package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import team.project.module.user.export.model.annotation.UserIdConstraint;
import team.project.module.user.internal.model.annotation.UserPasswordConstraint;

@Getter
@Setter
public class UserIdAndPasswordReq {

    @NotBlank(message="学号/工号不能为空")
    @UserIdConstraint
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message="密码不能为空")
    @UserPasswordConstraint
    @JsonProperty("pwd")
    private String password;
}
