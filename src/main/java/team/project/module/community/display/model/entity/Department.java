package team.project.module.community.display.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Timestamp;


@Data
@TableName("tbl_department")
public class Department {
    private Long id; // 自增主键
    @TableField("is_deleted") // 指定数据库列名为 is_deleted
    private Boolean isDeleted; // 是否逻辑删除
    @TableField("create_time")
    private Timestamp createTime; // 创建时间
    @TableField("updata_time")
    private Timestamp updateTime; // 更新时间
    private String abbreviation; // 简称
    @TableField("full_name") // 指定数据库列名为 full_name
    private String fullName; // 全称
}
