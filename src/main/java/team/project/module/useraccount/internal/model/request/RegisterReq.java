package team.project.module.useraccount.internal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterReq {
    @NotNull(message="学号/工号不能为空")
    @Size(min=1, max=20, message="学号/工号的长度不合约束")
    @JsonProperty("user_id")
    private String userId;

    @NotNull(message="所属院系id不能为空")
    @JsonProperty("department_id")
    private Long departmentId;

    @NotNull(message="密码不能为空")
    @Size(min=1, max=512, message="密码的长度不合约束")
    @JsonProperty("pwd")
    private String password;

    @NotNull(message="姓名不能为空")
    @Size(min=1, max=16, message="姓名的长度不合约束")
    @JsonProperty("name")
    private String name;

    @NotNull(message="电话号码不能为空")
    @Size(min=1, max=16, message="电话号码的长度不合约束")
    @JsonProperty("tel")
    private String tel;

    @NotNull(message="邮箱不能为空")
    @Size(min=1, max=100, message="邮箱的长度不合约束")
    @JsonProperty("mail")
    private String email;

    @NotNull(message="未选择用户角色")
    @Pattern(regexp="^(student|teacher)$", message="用户角色只能在“student”和“teacher”中二选一")
    @JsonProperty("role")
    private String role;
}
