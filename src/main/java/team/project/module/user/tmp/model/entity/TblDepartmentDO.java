package team.project.module.user.tmp.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tbl_department")
public class TblDepartmentDO {
    @TableId(value="id")               Long      id;
    @TableField(value="is_deleted")    Boolean   deleted;
    @TableField(value="create_time")   Timestamp createTime;
    @TableField(value="update_time")   Timestamp updateTime;
    @TableField(value="abbreviation")  String    abbreviation;
    @TableField(value="full_name")     String    fullName;
}
