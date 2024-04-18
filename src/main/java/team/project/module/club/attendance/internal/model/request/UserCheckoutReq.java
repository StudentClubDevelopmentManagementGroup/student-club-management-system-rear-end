package team.project.module.club.attendance.internal.model.request;

import lombok.Data;


import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 社团成员签退请求体
 */
@Data
public class UserCheckoutReq {
    private Long id;
    private String userId;
    private Long clubId;
    private LocalDateTime checkoutTime;
}
