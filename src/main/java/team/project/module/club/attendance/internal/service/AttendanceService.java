package team.project.module.club.attendance.internal.service;


import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import java.util.List;



public interface AttendanceService {

    //签到返回签到信息
    AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq);

    //查询社团成员当天最新的签到记录
    AttendanceInfoVO getLatestCheckInRecord(String userId, Long clubId);



    //签退返回签到信息
    AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq);

    //负责人补签
    AttendanceInfoVO makeUpAttendance(ApplyAttendanceReq applyAttendanceReq);



    //查社团一个成员指定时间打卡时长
    Long getOneAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq);

    //查询社团每个成员指定时间段打卡时长
    List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq);


    //查社团所有成员指定时间打卡记录
    List<AttendanceInfoVO> getEachAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq);


    //每天23.59.59逻辑删除当天未签退的记录
    int timedDeleteRecord();

}
