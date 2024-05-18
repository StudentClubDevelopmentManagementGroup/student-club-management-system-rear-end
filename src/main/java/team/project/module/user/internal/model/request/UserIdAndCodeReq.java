package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Getter
@Setter
public class UserIdAndCodeReq {

    @NotBlank(message="学号/工号不能为空")
    @UserIdConstraint
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message="验证码不能为空")
    @JsonProperty("code")
    private String code;
}
