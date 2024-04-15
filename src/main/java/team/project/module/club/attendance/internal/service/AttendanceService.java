package team.project.module.club.attendance.internal.service;

import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.DayCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckoutReq;

import java.util.List;

public interface AttendanceService {
    //签到
    boolean userCheckIn(UserCheckInReq userCheckinReq);
    //查当天签到记录
    List<AttendanceDO> getDayCheckIn(DayCheckInReq dayCheckInReq);

    //签退
    boolean userCheckOut(UserCheckoutReq userCheckoutReq);


}