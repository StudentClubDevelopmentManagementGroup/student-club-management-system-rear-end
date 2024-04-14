package team.project.module.community.display.model.request.attendence;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class UserCheckoutReq {
    private Long id;
    private String userId;
    private Long clubId;
    private LocalDateTime checkoutTime;
}
