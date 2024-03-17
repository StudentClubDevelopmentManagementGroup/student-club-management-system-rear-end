package team.project.module.manage_lin.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/* DO (Domain Object) 领域对象
   与数据库表结构一一对应 */
@Data
@TableName("tbl_club_DO")
public class tbl_club_DO {

//    @TableField(value="str") String str;
//    create table tbl_club (
//    `id`          bigint   not null auto_increment comment '自增主键',
//    `is_deleted`  tinyint  not null default 0      comment '是否逻辑删除',
//    `create_time` datetime not null default now()  comment '创建时间',
//            `update_time` datetime not null default now()  comment '更新时间',
//
//            `department_id` bigint       not null comment '所属院系',
//            `name`          varchar(128) not null comment '社团名称',
//            `state`         smallint     not null comment '状态',
//            -- 0b__1 数据正常
//    -- 0b_0_ 不招收社员
//    -- 0b_1_ 招收社员
//    primary key (`id`),
//    foreign key (`department_id`) REFERENCES `tbl_department`(`id`)
//            ) comment '基地表';

    @TableId(value="id")               Long      id;
    @TableField(value="is_deleted")    Boolean   deleted;
    @TableField(value="create_time")   Timestamp createTime;
    @TableField(value="update_time")   Timestamp updateTime;
    @TableField(value="department_id") Long      departmentId;
    @TableField(value="name")          String    name;
    @TableField(value="state")         Integer   state;
}
