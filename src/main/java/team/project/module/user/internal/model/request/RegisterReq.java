package team.project.module.user.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import team.project.module.user.internal.model.annotation.UserPasswordConstraint;
import team.project.module.user.export.model.annotation.UserIdConstraint;

@Data
public class RegisterReq {

    @NotBlank(message="学号/工号不能为空")
    @UserIdConstraint
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message="学号/工号不能为空")
    @UserPasswordConstraint
    @JsonProperty("pwd")
    private String password;

    @NotNull(message="所属院系id不能为空")
    @JsonProperty("department_id")
    private Long departmentId;

    @NotBlank(message="姓名不能为空")
    @Size(min=1, max=16, message="姓名的长度不合约束")
    @JsonProperty("name")
    private String name;

    @NotBlank(message="电话号码不能为空")
    @Size(min=1, max=16, message="电话号码的长度不合约束")
    @JsonProperty("tel")
    private String tel;

    @Email
    @NotBlank(message="邮箱不能为空")
    @Size(min=1, max=100, message="邮箱的长度不合约束")
    @JsonProperty("mail")
    private String email;

    @NotBlank(message="未选择用户角色")
    @Pattern(regexp="^(student|teacher)$", message="用户角色只能在“student”和“teacher”中二选一")
    @JsonProperty("role")
    private String role;
}
