package team.project.module.club.attendance.internal.service.impl;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.attendance.internal.mapper.AttendanceMapper;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import team.project.module.club.attendance.internal.service.AttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.project.module.user.export.service.UserInfoIService;

import java.util.ArrayList;
import java.util.List;



@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, AttendanceDO> implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private UserInfoIService userInfoIService;

    @Override
    //签到返回签到信息
    public AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq){
        return attendanceMapper.userCheckIn(userCheckinReq);

    }


    @Override
    //签退返回签到信息
    public AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq){
        AttendanceDO attendanceDO = attendanceMapper.userCheckOutTest(userCheckoutReq);
        if(attendanceDO !=null){
            AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();
            String userName = userInfoIService.selectUserBasicInfo(attendanceDO.getUserId()).getName();
            attendanceInfoVO.setUserName(userName);
            BeanUtils.copyProperties(attendanceDO, attendanceInfoVO);
            return attendanceInfoVO;
        }else {
            return null;
        }
    }

    //查询社团成员当天最新的签到记录
    @Override
    public AttendanceInfoVO getLatestCheckInRecord(String userId, Long clubId){
        if(attendanceMapper.getLatestCheckInRecord(userId,clubId) != null) {
            //BeanUtils.copyProperties方法将一个对象的属性复制到另一个对象
            // 复制空对象会引发异常
            AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();
            BeanUtils.copyProperties(attendanceMapper.getLatestCheckInRecord(userId,clubId), attendanceInfoVO);
            return attendanceInfoVO;
        }else{
            return null;
        }
    }



    @Override
    //查社团一个成员指定时间打卡时长
    public Long getOneAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq){
        return attendanceMapper.getOneAttendanceDurationTime(getAttendanceTimeReq);
    }
    @Override
    //查询社团每个成员指定时间段打卡时长
    public List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq){
        List<ClubAttendanceDurationVO> clubAttendanceDurationVOList =
                attendanceMapper.getEachAttendanceDurationTime(getAttendanceTimeReq);
        for (ClubAttendanceDurationVO clubAttendanceDurationVO : clubAttendanceDurationVOList){
            String userName = userInfoIService.selectUserBasicInfo(clubAttendanceDurationVO.getUserId()).getName();
            clubAttendanceDurationVO.setUserName(userName);
        }
        return clubAttendanceDurationVOList;
    }

    @Override
    //查社团成员指定时间打卡记录
    public List<AttendanceInfoVO> getAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq){
        List<AttendanceInfoVO> attendanceInfoVOList = attendanceMapper.getAttendanceRecord(getAttendanceRecordReq);
        for(AttendanceInfoVO attendanceInfoVO : attendanceInfoVOList){
            String userName = userInfoIService.selectUserBasicInfo(attendanceInfoVO.getUserId()).getName();
            attendanceInfoVO.setUserName(userName);
        }
        return attendanceInfoVOList;
    }


    //定时逻辑删除签到记录
    @Override
    public int timedDeleteRecord() {
            return attendanceMapper.timedDeleteRecord();
    }
}



