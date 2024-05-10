package team.project.module.club.attendance.internal.service;

import team.project.base.model.view.PageVO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;


import java.util.List;



public interface AttendanceService {


    //签到返回签到信息
    AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq);


    //查询社团成员当天最新的签到记录
    AttendanceInfoVO getLatestCheckInRecord(String userId, String clubName);


    //签退返回签到信息
    AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq);


    //查询社团成员指定时间段打卡时长
    List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq);

    //查打卡记录
    PageVO<AttendanceInfoVO> getAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq);


    //社团成员申请补签
    AttendanceInfoVO userReplenishAttendance(ApplyAttendanceReq applyAttendanceReq);



    //每天23.59.59逻辑删除当天未签退的记录
    int timedDeleteRecord();

}
