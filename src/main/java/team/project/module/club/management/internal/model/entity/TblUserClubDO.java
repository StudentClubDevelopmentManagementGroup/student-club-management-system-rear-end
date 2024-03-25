package team.project.module.club.management.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/* DO (Domain Object) 领域对象
   与数据库表结构一一对应 */
@Data
@TableName("tbl_club")
public class TblUserClubDO {


//-- 基地成员表（用户-基地 关系表）
//            -- 报名了社团，但还不是正式社团成员的，不存于该表
//    drop table if exists `tbl_user_club`;
//    create table `tbl_user_club` (
//            `id`          bigint   not null auto_increment comment '自增主键',
//            `is_deleted`  tinyint  not null default 0      comment '是否逻辑删除',
//            `create_time` datetime not null default now()  comment '创建时间',
//            `update_time` datetime not null default now()  comment '更新时间',
//
//            `user_id`     bigint   not null comment '用户',
//            `club_id`     bigint   not null comment '基地',
//            `role`        smallint not null comment '身份',
//            -- 0b__1 社团成员
//    -- 0b_1_ 负责人
//
//    primary key (`id`),
//    foreign key (`user_id`) REFERENCES `tbl_user`(`id`),
//    foreign key (`club_id`) REFERENCES `tbl_club`(`id`)
//            ) comment '基地成员表（用户-基地 关系表）';

    @TableId(value="id")               Long      id;
    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")    Boolean   isDeleted;
    @TableField(value="create_time")   Timestamp createTime;
    @TableField(value="update_time")   Timestamp updateTime;
    @TableField(value="user_id")       Long      userId;
    @TableField(value="club_id")       String    clubId;
    @TableField(value="role")          Integer   role;
}
