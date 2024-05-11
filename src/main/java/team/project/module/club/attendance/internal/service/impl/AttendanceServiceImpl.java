package team.project.module.club.attendance.internal.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.attendance.internal.mapper.AttendanceMapper;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import team.project.module.club.attendance.internal.service.AttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.project.module.club.attendance.internal.util.ToolMethods;
import team.project.module.club.management.export.servivce.ManagementIService;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.service.UserInfoIService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
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

    @Autowired
    private PceIService pceIService;




    @Override
    //签到返回签到信息
    public AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq){
        LocalDateTime checkInTime = userCheckinReq.getCheckInTime();
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 校验签到时间是否为今天的日期且大于等于当前时间
        LocalDateTime oneMinuteAgo = now.minus(Duration.ofMinutes(1));
        if ( checkInTime.toLocalDate().isEqual(now.toLocalDate())
                && checkInTime.isBefore(now)
                && checkInTime.isAfter(oneMinuteAgo)) {
            Long clubId = managementIService.selectClubIdByName(userCheckinReq.getClubName());
            if(!pceIService.isClubMember(userCheckinReq.getUserId(),clubId)) {
                throw new ServiceException(ServiceStatus.BAD_REQUEST, "该社团没有这个成员");
            }

            // 查询当天最新的签到记录
            AttendanceDO latestCheckInRecord = attendanceMapper.getLatestCheckInRecord(
                            userCheckinReq.getUserId(), clubId);

            //如果当天的最新签到记录存在且未进行签退
            if( latestCheckInRecord != null && latestCheckInRecord.getCheckoutTime() == null ){
                throw new ServiceException(ServiceStatus.BAD_REQUEST, "签到失败，上一次签到未签退");
            }
            //满足签到条件进行签到
            else {

                AttendanceDO attendanceDO = attendanceMapper.userCheckIn(userCheckinReq,clubId);
                return toolMethods.convert(attendanceDO);
            }
        //签到时间不合理，不符合当前时间
        } else {
            throw new ServiceException(ServiceStatus.BAD_REQUEST, "签到时间不合理");
        }

    }


    @Override
    public AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq) {
        //获取签退时间
        LocalDateTime checkoutTime = userCheckoutReq.getCheckoutTime();
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 校验签到时间是否为今天的日期且大于等于当前时间
        LocalDateTime oneMinuteAgo = now.minus(Duration.ofMinutes(1));
        if (checkoutTime.toLocalDate().isEqual(now.toLocalDate())
                && checkoutTime.isBefore(now)
                && checkoutTime.isAfter(oneMinuteAgo)) {
            Long clubId = managementIService.selectClubIdByName(userCheckoutReq.getClubName());
            if(!pceIService.isClubMember(userCheckoutReq.getUserId(),clubId)) {
                throw new ServiceException(ServiceStatus.BAD_REQUEST, "该社团没有这个成员");
            }

            AttendanceDO attendanceDO= attendanceMapper.userCheckOut(userCheckoutReq,clubId);

            if(attendanceDO != null){
                return toolMethods.convert(attendanceDO);

            }else {
                throw new ServiceException(ServiceStatus.BAD_REQUEST, "没有可以签退的记录");
            }

        }else {
            throw new ServiceException(ServiceStatus.BAD_REQUEST, "签退时间不合理");
        }
    }


    //查询社团成员当天最新的签到记录
    @Override
    public AttendanceInfoVO getLatestCheckInRecord(String userId, String clubName){
        Long clubId = managementIService.selectClubIdByName(clubName);
        if(attendanceMapper.getLatestCheckInRecord(userId,clubId) != null) {
            return toolMethods.convert(attendanceMapper.getLatestCheckInRecord(userId,clubId));
        }else  {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "该学生今天未签到");
        }
    }


    @Override
    //查询社团成员指定时间段打卡时长
    public List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq){
        Long clubId = managementIService.selectClubIdByName(getAttendanceTimeReq.getClubName());
        // 根据用户名字查询学号
        List<UserBasicInfoDTO> users = userInfoIService.searchUsers(getAttendanceTimeReq.getUserName());
        List<String> userIds = new ArrayList<>();
        for (UserBasicInfoDTO user : users) {
            userIds.add(user.getUserId());
        }

        if(userIds.isEmpty()) {throw new ServiceException(ServiceStatus.NOT_FOUND, "没有该学生签到信息");}

//        if(getAttendanceTimeReq.getUserId() != ""){
//            if(!pceIService.isClubMember(getAttendanceTimeReq.getUserId(),clubId)) {
//                throw new ServiceException(ServiceStatus.BAD_REQUEST, "该社团没有这个成员");
//            }
//        }
//        List<ClubAttendanceDurationVO> clubAttendanceDurationVOList =
//                attendanceMapper.getEachAttendanceDurationTime(getAttendanceTimeReq,clubId);

        List<ClubAttendanceDurationVO> clubAttendanceDurationVOList =
                attendanceMapper.getEachAttendanceDurationTimeTest(getAttendanceTimeReq,clubId,userIds);

        for (ClubAttendanceDurationVO clubAttendanceDurationVO : clubAttendanceDurationVOList){

            String userName = userInfoIService.selectUserBasicInfo(clubAttendanceDurationVO.getUserId()).getName();
            clubAttendanceDurationVO.setUserName(userName);


            clubAttendanceDurationVO.setClubName(getAttendanceTimeReq.getClubName());
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

        if(userIds.isEmpty()) {
            throw new ServiceException(ServiceStatus.NOT_FOUND, "没有该学生签到信息");
        }

//        if(getAttendanceRecordReq.getUserId() != ""){
//            if(!pceIService.isClubMember(getAttendanceRecordReq.getUserId(),clubId)) {
//                System.out.println("字符串为空测试");
//                throw new ServiceException(ServiceStatus.BAD_REQUEST, "该社团没有这个成员");
//            }
//        }




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
        if(!applyAttendanceReq.getCheckInTime().toLocalDate()
                .equals(applyAttendanceReq.getCheckoutTime().toLocalDate())) {
            throw new ServiceException(ServiceStatus.BAD_REQUEST, "时间不合理，签到时间与签退时间不在同一天");
        }
        Long clubId = managementIService.selectClubIdByName(applyAttendanceReq.getClubName());
        if(!pceIService.isClubMember(applyAttendanceReq.getUserId(),clubId)) {
            throw new ServiceException(ServiceStatus.BAD_REQUEST, "该社团没有这个成员");
        }
        AttendanceDO attendanceDO = attendanceMapper.userReplenishAttendance(applyAttendanceReq,clubId);
        if(attendanceDO ==null ) {
            throw new ServiceException(ServiceStatus.BAD_REQUEST, "该成员当天没有记录未签退");
        }
        return toolMethods.convert(attendanceDO);


    }


    //定时逻辑删除签到记录
    @Override
    public int timedDeleteRecord() {
            return attendanceMapper.timedDeleteRecord();
    }
}



