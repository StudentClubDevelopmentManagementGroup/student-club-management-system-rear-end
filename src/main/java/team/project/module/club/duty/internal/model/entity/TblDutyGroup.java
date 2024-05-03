package team.project.module.club.duty.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("tbl_user_club_duty_group")
public class TblDutyGroup {
    @TableField(value="id")          Long      id;
    @TableField(value="club_id")     Long      club_id;
    @TableField(value="member_id")   String    member_id;
    @TableField(value="name")        String    name;
}
