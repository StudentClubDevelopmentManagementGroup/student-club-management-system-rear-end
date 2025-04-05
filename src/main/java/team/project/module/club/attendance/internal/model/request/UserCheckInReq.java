package team.project.module.club.attendance.internal.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 社团成员签到请求体
 */
@Data
public class UserCheckInReq {
    @NotNull(message = "社团id不能为空")
    private Long clubId;

    @NotNull(message = "用户id不能为空")
    private String userId;
    // 移除 checkInTime 字段
}
