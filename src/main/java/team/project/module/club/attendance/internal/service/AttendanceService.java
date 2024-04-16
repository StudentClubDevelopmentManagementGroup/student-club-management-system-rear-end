package team.project.module.club.attendance.internal.service;

import org.apache.ibatis.annotations.Param;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.ApplyAttendanceReq;
import team.project.module.club.attendance.internal.model.request.DayCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckoutReq;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import java.util.List;

public interface AttendanceService {
    //签到
    boolean userCheckIn(UserCheckInReq userCheckinReq);
    //查当天签到记录
    List<AttendanceDO> getDayCheckIn(DayCheckInReq dayCheckInReq);

    //签退
    boolean userCheckOut(UserCheckoutReq userCheckoutReq);

    //负责人补签
    AttendanceInfoVO makeUpAttendance(ApplyAttendanceReq applyAttendanceReq);

    //查询本周签到时长
    //查询社团成员一个月的打卡时长

    Long getTotalMonthSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId,
            @Param("year") int year,
            @Param("month") int month
    );


    //查询社团成员一年的打卡时长
    Long getTotalYearSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId,
            @Param("year") int year

    );
}
