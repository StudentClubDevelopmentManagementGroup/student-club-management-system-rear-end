package team.project.module.user.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import team.project.module.user.export.model.enums.UserRole;

@Data
@TableName("tbl_user")
public class TblUserDO {
    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")    private Boolean   deleted;
    @TableId(value="id")               private Long      id;
    @TableField(value="user_id")       private String    userId;
    @TableField(value="department_id") private Long      departmentId;
    @TableField(value="pwd")           private String    password;
    @TableField(value="name")          private String    name;
    @TableField(value="tel")           private String    tel;
    @TableField(value="mail")          private String    email;
    @TableField(value="role")          private Integer   role;

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(UserRole role) {
        return UserRole.hasRole(this.role, role);
    }

    /* 更新用户的身份，使其拥有指定角色 */
    public void addRole(UserRole role) {
        this.role = UserRole.addRole(this.role, role);
    }

    /* 移除指定角色 */
    public void removeRole(UserRole role) {
        this.role = UserRole.removeRole(this.role, role);
    }
}
