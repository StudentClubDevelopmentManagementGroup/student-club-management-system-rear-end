package team.project.module._template.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/* DO (Domain Object) 领域对象
   与数据库表结构一一对应 */
@Data
@TableName("tbl_tmp_te4st")
public class TmplDO {

    /*
    create table `tbl_tmp_test` (
        `id`          bigint   not null auto_increment                comment '自增主键',
        `is_deleted`  tinyint  not null default 0                     comment '逻辑删除',
        `create_time` datetime not null default now()                 comment '创建时间',
        `update_time` datetime not null default now() on update now() comment '更新时间',

        `str`         varchar(96) default '' not null comment '文本'
    ) comment '测试用';
    */

    /* 示例（与 tbl_tmp_test 表结构对应） */
    @TableId(value="id")             Long      id;
    @TableField(value="is_deleted")  Boolean   isDeleted;
    @TableField(value="create_time") Timestamp createTime;
    @TableField(value="update_time") Timestamp updateTime;

    @TableField(value="str") String str;
}
