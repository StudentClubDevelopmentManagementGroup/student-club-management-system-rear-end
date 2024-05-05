package team.project.module.club.attendance.internal.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.module.club.attendance.internal.mapper.AttendanceMapper;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import team.project.module.club.attendance.internal.service.AttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.project.module.club.attendance.internal.util.ToolMethods;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.export.servivce.ManagementIService;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
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

    @Autowired
    public ManagementIService managementIService;





    @Override
    //签到返回签到信息
    public AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq){

        Long clubId = managementIService.selectClubIdByName(userCheckinReq.getClubName());

        AttendanceDO attendanceDO = attendanceMapper.userCheckIn(userCheckinReq,clubId);
        // 如果插入成功，则设置签到信息的ID属性
        if (attendanceDO !=null) {
            return toolMethods.convert(attendanceDO);
        }
        return null;
    }


    @Override
    //签退返回签到信息
    public AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq){
        Long clubId = managementIService.selectClubIdByName(userCheckoutReq.getClubName());
        AttendanceDO attendanceDO = attendanceMapper.userCheckOut(userCheckoutReq,clubId);
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

    //查询社团成员当天最新的签到记录
    @Override
    public AttendanceInfoVO getLatestCheckInRecordTest(String userId, String clubName){
        Long clubId = managementIService.selectClubIdByName(clubName);
        System.out.println("社团id"  + clubId);
        if(attendanceMapper.getLatestCheckInRecord(userId,clubId) != null) {
            return toolMethods.convert(attendanceMapper.getLatestCheckInRecord(userId,clubId));
        }else{
            return null;
        }
    }




    @Override
    //查询社团成员指定时间段打卡时长
    public List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq){
        Long clubId = managementIService.selectClubIdByName(getAttendanceTimeReq.getClubName());
        List<ClubAttendanceDurationVO> clubAttendanceDurationVOList =
                attendanceMapper.getEachAttendanceDurationTime(getAttendanceTimeReq,clubId);
        for (ClubAttendanceDurationVO clubAttendanceDurationVO : clubAttendanceDurationVOList){
            String userName = userInfoIService.selectUserBasicInfo(clubAttendanceDurationVO.getUserId()).getName();
            clubAttendanceDurationVO.setUserName(userName);

            ClubBasicMsgDTO clubBasicMsgDTO = managementIService.selectClubBasicMsg(clubId);
            String clubName = clubBasicMsgDTO.getName();
            clubAttendanceDurationVO.setClubName(clubName);
        }
        return clubAttendanceDurationVOList;
    }


    //查签到记录优化分页
    @Override
    public PageVO<AttendanceInfoVO> getAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq) {


        Long clubId = managementIService.selectClubIdByName(getAttendanceRecordReq.getClubName());

        // 根据用户名字查询学号
        List<UserBasicInfoDTO> users = userInfoIService.searchUsers(getAttendanceRecordReq.getUserName());
        List<String> userIds = new ArrayList<>();
        for (UserBasicInfoDTO user : users) {
            userIds.add(user.getUserId());
        }
//        Page<AttendanceDO> page = attendanceMapper.findAttendanceInfoVOPage(getAttendanceRecordReq,clubId);
        Page<AttendanceDO> page = attendanceMapper.findAttendanceInfoVOPageTest(getAttendanceRecordReq,clubId,userIds);
        List<AttendanceInfoVO> result = new ArrayList<>();
        for (AttendanceDO attendanceDO : page.getRecords()) {
            result.add(toolMethods.convert(attendanceDO));
        }
        return new PageVO<>(result, page);
    }


    //社团成员申请补签,返回补签记录
    public AttendanceInfoVO userReplenishAttendance(ApplyAttendanceReq applyAttendanceReq){
        Long clubId = managementIService.selectClubIdByName(applyAttendanceReq.getClubName());
            AttendanceDO attendanceDO = attendanceMapper.userReplenishAttendance(applyAttendanceReq,clubId);
            return toolMethods.convert(attendanceDO);


    }


    //定时逻辑删除签到记录
    @Override
    public int timedDeleteRecord() {
            return attendanceMapper.timedDeleteRecord();
    }
}



