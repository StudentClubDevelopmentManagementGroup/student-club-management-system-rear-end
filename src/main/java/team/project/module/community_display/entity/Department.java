package team.project.module.community_display.entity;

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
    private Integer is_deleted; // 是否逻辑删除
    private Date create_time; // 创建时间
    private Date update_time; // 更新时间
    private String abbreviation; // 简称
    private String full_name; // 全称

}
