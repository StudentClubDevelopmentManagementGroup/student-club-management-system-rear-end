package team.project.module.community.display.entity;


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

@TableName("tbl_user_club")
public class UserClub {
    private Long id; // 自增主键
    private Integer isDeleted; // 是否逻辑删除
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
    private Long userId; // 用户
    private Long clubId; // 基地
    private Integer role; // 身份
}
