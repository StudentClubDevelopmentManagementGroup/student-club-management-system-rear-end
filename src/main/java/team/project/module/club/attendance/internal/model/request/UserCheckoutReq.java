package team.project.module.club.attendance.internal.model.request;

import lombok.Data;


import java.time.LocalDateTime;


@Data
public class UserCheckoutReq {
    private Long id;
    private String userId;
    private Long clubId;
    private LocalDateTime checkoutTime;
}
