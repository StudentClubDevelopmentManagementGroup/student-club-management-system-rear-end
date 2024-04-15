package team.project.module.community.display.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Club {
    private Long id;
    @TableField("is_deleted") // 指定数据库列名为 is_deleted
    private Boolean isDeleted;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("updata_time")
    private LocalDateTime updateTime;
    @TableField("department_id")
    private Long departmentId;
    private String name;
    private Integer state;


}
