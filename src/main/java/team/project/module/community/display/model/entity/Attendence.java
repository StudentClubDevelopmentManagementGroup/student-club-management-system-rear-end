package team.project.module.community.display.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
@Data
@TableName("tbl_user_club_attendance")  //使用Mybatis-plus时必须使用注解指定对应数据库哪一张表
public class Attendence {

    private Long id;
    @TableField("is_deleted") // 指定数据库列名为 is_deleted
    private boolean isDeleted;
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;
    @TableField("checkin_time")
    private Timestamp checkinTime;
    @TableField("checkout_time")
    private Timestamp checkoutTime;
    @TableField("user_id")
    private String userId;
    @TableField("club_id")
    private Long clubId;
}
