package team.project.module.community.display.model.request.attendence;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class UserCheckoutReq {
    private Long id;
    private String userId;
    private Long clubId;
    private Timestamp checkoutTime;
}
