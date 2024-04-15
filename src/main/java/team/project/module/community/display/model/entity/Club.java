package team.project.module.community.display.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class Club {
    private Long id;
    @TableField("is_deleted") // 指定数据库列名为 is_deleted
    private Boolean isDeleted;
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("updata_time")
    private Timestamp updateTime;
    @TableField("department_id")
    private Long departmentId;
    private String name;
    private Integer state;


}
