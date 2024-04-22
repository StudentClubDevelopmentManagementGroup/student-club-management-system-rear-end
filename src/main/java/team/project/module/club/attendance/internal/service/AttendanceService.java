package team.project.module.club.attendance.internal.service;

import org.apache.ibatis.annotations.Param;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;

import java.time.LocalDateTime;
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
    //查询社团成员一年的打卡时长
    Long getTotalWeekSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId


    );

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



    //查社团一个成员指定时间打卡时长
    Long getAnyDurationSecondsT(GetOneAnyDurationReq getOneAnyDurationReq);

    //查询社团每个成员一个月打卡时长List<DepartmentVO>
    List<ClubAttendanceDurationVO> getEachTotalMonthDuration(
            @Param("clubId") Long clubId,
            @Param("year") int year,
            @Param("month") int month

    );

    //查询社团每个成员每年打卡时长
    List<ClubAttendanceDurationVO> getEachTotalYearDuration(
            @Param("clubId") Long clubId,
            @Param("year") int year

    );

    //查询社团每个成员本周打卡时长
    List<ClubAttendanceDurationVO> getEachTotalWeekDuration(@Param("clubId") Long clubId);



    //查询社团每个成员指定时间段打卡时长
    List<ClubAttendanceDurationVO> getEachTotalAnyDuration(GetEachAnyDurationReq getEachAnyDurationReq);

}
