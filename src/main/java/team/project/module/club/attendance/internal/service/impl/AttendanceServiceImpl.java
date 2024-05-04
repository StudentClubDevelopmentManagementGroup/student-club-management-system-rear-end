package team.project.module.club.attendance.internal.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.PageVO;
import team.project.module.club.attendance.internal.mapper.AttendanceMapper;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import team.project.module.club.attendance.internal.service.AttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.project.module.club.attendance.internal.util.ToolMethods;
import team.project.module.user.export.service.UserInfoIService;

import java.util.ArrayList;
import java.util.List;



@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, AttendanceDO> implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private UserInfoIService userInfoIService;
    @Autowired
    private ToolMethods toolMethods;


    @Override
    //签到返回签到信息
    public AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq){
        AttendanceDO attendanceDO = attendanceMapper.userCheckIn(userCheckinReq);
        // 如果插入成功，则设置签到信息的ID属性
        if (attendanceDO !=null) {
            return toolMethods.convert(attendanceDO);
        }
        return null;
    }


    @Override
    //签退返回签到信息
    public AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq){
        AttendanceDO attendanceDO = attendanceMapper.userCheckOut(userCheckoutReq);
        if(attendanceDO !=null){
            return toolMethods.convert(attendanceDO);
        }else {
            return null;
        }
    }

    //查询社团成员当天最新的签到记录
    @Override
    public AttendanceInfoVO getLatestCheckInRecord(String userId, Long clubId){
        if(attendanceMapper.getLatestCheckInRecord(userId,clubId) != null) {

            return toolMethods.convert(attendanceMapper.getLatestCheckInRecord(userId,clubId));
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


    //查签到记录优化分页
    @Override
    public PageVO<AttendanceInfoVO> getAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq) {

        Page<AttendanceDO> page = attendanceMapper.findAttendanceInfoVOPage(getAttendanceRecordReq);
//        if(getAttendanceRecordReq.getUserId() != ""){
//            String userName = userInfoIService.selectUserBasicInfo(getAttendanceRecordReq.getUserId()).getName();
//        }

        List<AttendanceInfoVO> result = new ArrayList<>();
        for (AttendanceDO attendanceDO : page.getRecords()) {
            //自定义转换器方法
            result.add(toolMethods.convert(attendanceDO));
        }
        return new PageVO<>(result, page);
    }

    //社团成员申请补签,返回补签记录
    public AttendanceInfoVO userReplenishAttendance(ApplyAttendanceReq applyAttendanceReq){
            AttendanceDO attendanceDO = attendanceMapper.userReplenishAttendance(applyAttendanceReq);
            return toolMethods.convert(attendanceDO);


    }


    //定时逻辑删除签到记录
    @Override
    public int timedDeleteRecord() {
            return attendanceMapper.timedDeleteRecord();
    }
}



