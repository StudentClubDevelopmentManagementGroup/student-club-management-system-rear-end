package team.project.module.community.display.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Club {
    private Long id;
    @TableField("is_deleted") // 指定数据库列名为 is_deleted
    private int isDeleted;
    @TableField("create_time")
    private Date createTime;
    @TableField("updata_time")
    private Date updateTime;
    @TableField("department_id")
    private Long departmentId;
    private String name;
    private int state;



}
