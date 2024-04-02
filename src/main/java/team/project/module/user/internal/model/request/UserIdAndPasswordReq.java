package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserIdAndPasswordReq {

    /* 这部分字段的约束与 RegisterReq 保持一致 [begin] */

    @NotBlank(message="学号/工号不能为空")
    @Size(min=1, max=20, message="学号/工号的长度不合约束")
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message="密码不能为空")
    @Size(min=1, max=512, message="密码的长度不合约束")
    @JsonProperty("pwd")
    private String password;

    /* 这部分字段的约束与 RegisterReq 保持一致 [end] */
}
