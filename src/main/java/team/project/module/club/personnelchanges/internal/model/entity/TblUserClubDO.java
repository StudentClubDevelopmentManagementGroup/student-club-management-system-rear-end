package team.project.module.club.personnelchanges.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tbl_user_club")
public class TblUserClubDO {
    @TableId(value="id")               Long      id;
    @TableLogic(value="0", delval="1")
    @TableField(value="is_deleted")    Boolean   isDeleted;
    @TableField(value="create_time")   Timestamp createTime;
    @TableField(value="update_time")   Timestamp updateTime;
    @TableField(value="user_id")       String    userId;
    @TableField(value="club_id")       Long      clubId;
    @TableField(value="role")          Integer   role;

    public boolean isMember() {
        return (this.role & 1) != 0; /* review TODO 删除 0 个用法的函数 */
    }

    public boolean isManager() {
        return (this.role & 2) != 0;
    }
}
