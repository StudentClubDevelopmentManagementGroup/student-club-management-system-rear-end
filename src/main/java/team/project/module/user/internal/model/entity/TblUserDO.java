package team.project.module.user.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import team.project.module.user.internal.model.enums.UserRole;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@TableName("tbl_user")
public class TblUserDO {
    @TableId(value="id")               Long      id;
    @TableLogic(value="0", delval="1") /* <- ? */
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
    public boolean hasRole(UserRole role) {
        assert this.role != null : "当前角色码为 null";
        return UserRole.hasRole(this.role, role);
    }

    /* 更新用户的身份，使其拥有指定角色 */
    public void addRole(UserRole role) {
        assert this.role != null : "当前角色码为 null，请先 setRole(UserRole.getEmptyRoleCode())";
        this.role = UserRole.addRole(this.role, role);
    }

    /* 移除指定角色 */
    public void removeRole(UserRole role) {
        assert this.role != null : "当前角色码为 null";
        this.role = UserRole.removeRole(this.role, role);
    }

    /* 获取用户的身份描述列表 */
    public ArrayList<String> getRoleList() {
        assert this.role != null : "当前角色码为 null";
        return UserRole.getExistingRoleDescriptions(this.role);
    }
}
