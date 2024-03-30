package team.project.module.user.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import team.project.module.user.internal.model.enums.UserRoleEnum;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@TableName("tbl_user")
public class TblUserDO {
    @TableId(value="id")               Long      id;
    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")    Boolean   deleted;
    @TableField(value="create_time")   Timestamp createTime;
    @TableField(value="update_time")   Timestamp updateTime;
    @TableField(value="user_id")       String    userId;
    @TableField(value="department_id") Long      departmentId;
    @TableField(value="pwd")           String    password;
    @TableField(value="name")          String    name;
    @TableField(value="tel")           String    tel;
    @TableField(value="mail")          String    email;
    @TableField(value="role")          Integer   role;

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(UserRoleEnum role) {
        assert this.role != null : "当前角色码为 null";
        return UserRoleEnum.hasRole(this.role, role);
    }

    /* 更新用户的身份，使其拥有指定角色 */
    public void addRole(UserRoleEnum role) {
        assert this.role != null : "当前角色码为 null，请先 setRole(UserRole.getEmptyRoleCode())";
        this.role = UserRoleEnum.addRole(this.role, role);
    }

    /* 移除指定角色 */
    public void removeRole(UserRoleEnum role) {
        assert this.role != null : "当前角色码为 null";
        this.role = UserRoleEnum.removeRole(this.role, role);
    }
}
