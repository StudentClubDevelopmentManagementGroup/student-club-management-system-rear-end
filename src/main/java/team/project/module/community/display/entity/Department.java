package team.project.module.community.display.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@TableName("tbl_department")
public class Department {
    private Long id; // 自增主键
    @TableField("is_deleted") // 指定数据库列名为 is_deleted
    private Integer isDeleted; // 是否逻辑删除
    @TableField("create_time")
    private Date createTime; // 创建时间
    @TableField("updata_time")
    private Date updateTime; // 更新时间
    private String abbreviation; // 简称
    @TableField("full_name") // 指定数据库列名为 full_name
    private String fullName; // 全称

}
