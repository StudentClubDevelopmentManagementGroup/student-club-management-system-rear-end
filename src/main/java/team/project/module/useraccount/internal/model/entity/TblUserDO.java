package team.project.module.useraccount.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@TableName("tbl_user")
public class TblUserDO {
    @TableId(value="id")               Long      id;
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

    @AllArgsConstructor
    public enum Role {
        ROLE_STUDENT     (0b00001, "学生"),
        ROLE_TEACHER     (0b00010, "教师"),
        ROLE_XXX         (0b00100, "？"),
        ROLE_SUPER_ADMIN (0b01000, "超级管理员"),
        ;

        public final int    v; /* <- value */
        public final String roleName;
    }

    /* 判断用户是否拥有指定角色 */
    public boolean hasRole(Role role) {
        if (null == this.role) {
            return false;
        }

        return switch (role) {
            case ROLE_STUDENT     -> 0 != (this.role & Role.ROLE_STUDENT.v);
            case ROLE_TEACHER     -> 0 != (this.role & Role.ROLE_TEACHER.v);
            case ROLE_XXX         -> 0 != (this.role & Role.ROLE_XXX.v);
            case ROLE_SUPER_ADMIN -> 0 != (this.role & Role.ROLE_SUPER_ADMIN.v);
        };
    }

    /* 更新用户的身份，使其拥有指定角色 */
    public void addRole(Role role) {
        if (null == this.role) {
            this.role = 0;
        }

        switch (role) {
            case ROLE_STUDENT -> {
                this.role &= ~Role.ROLE_TEACHER.v;
                this.role |= Role.ROLE_STUDENT.v;
            }
            case ROLE_TEACHER -> {
                this.role &= ~Role.ROLE_STUDENT.v;
                this.role |= Role.ROLE_TEACHER.v;
            }
            case ROLE_XXX         -> this.role |= Role.ROLE_XXX.v;
            case ROLE_SUPER_ADMIN -> this.role |= Role.ROLE_SUPER_ADMIN.v;
        };
    }

    /* 更新用户的身份，移除指定角色（不能移除教师或学生角色） */
    public void removeRole(Role role) {
        if (null == this.role) {
            return;
        }

        switch (role) {
            case ROLE_XXX         -> this.role &= ~Role.ROLE_XXX.v;
            case ROLE_SUPER_ADMIN -> this.role &= ~Role.ROLE_SUPER_ADMIN.v;
        };
    }

    /* 获取用户的身份描述列表 */
    public ArrayList<String> getRoleList() {
        ArrayList<String> roles = new ArrayList<>();
        if (hasRole(Role.ROLE_STUDENT))     roles.add(Role.ROLE_STUDENT.roleName);
        if (hasRole(Role.ROLE_TEACHER))     roles.add(Role.ROLE_TEACHER.roleName);
        if (hasRole(Role.ROLE_XXX))         roles.add(Role.ROLE_XXX.roleName);
        if (hasRole(Role.ROLE_SUPER_ADMIN)) roles.add(Role.ROLE_SUPER_ADMIN.roleName);
        return roles;
    }
}
