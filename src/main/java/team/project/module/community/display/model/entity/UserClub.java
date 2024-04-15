package team.project.module.community.display.model.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Timestamp;


@Data
@TableName("tbl_user_club")
public class UserClub {
    private Long id; // 自增主键
    private Boolean isDeleted; // 是否逻辑删除
    private Timestamp createTime; // 创建时间
    private Timestamp updateTime; // 更新时间
    private Long userId; // 用户
    private Long clubId; // 基地
    private Integer role; // 身份
}
