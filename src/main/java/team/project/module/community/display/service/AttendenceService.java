package team.project.module.community.display.service;

import team.project.module.community.display.model.entity.Attendence;
import team.project.module.community.display.model.request.attendence.DayCheckInReq;
import team.project.module.community.display.model.request.attendence.UserCheckinReq;
import team.project.module.community.display.model.request.attendence.UserCheckoutReq;

import java.time.LocalDate;
import java.util.List;

public interface AttendenceService {
    //签到
    public boolean userCheckIn(UserCheckinReq userCheckinReq);
    //查当天签到记录
    public List<Attendence> getDayCheckIn(DayCheckInReq dayCheckInReq);

    //签退
    public boolean userCheckOut(UserCheckoutReq userCheckoutReq);


}
