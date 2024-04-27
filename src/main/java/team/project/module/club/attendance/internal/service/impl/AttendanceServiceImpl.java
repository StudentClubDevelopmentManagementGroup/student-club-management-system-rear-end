package team.project.module.club.attendance.internal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
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
import team.project.module.user.export.service.UserInfoIService;

import java.time.LocalDate;
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
        AttendanceDO attendanceDO = attendanceMapper.userCheckIn(userCheckinReq);
        // 创建一个 AttendanceInfoVO 对象，用于存储签到信息
        AttendanceInfoVO attendanceInfo = new AttendanceInfoVO();
        // 如果插入成功，则设置签到信息的ID属性
        if (attendanceDO !=null) {
            attendanceInfo.setId(attendanceDO.getId());
            attendanceInfo.setUserId(userCheckinReq.getUserId());
            attendanceInfo.setClubId(userCheckinReq.getClubId());
            String userName = userInfoIService.selectUserBasicInfo(userCheckinReq.getUserId()).getName();
            attendanceInfo.setUserName(userName);
            attendanceInfo.setCheckInTime(userCheckinReq.getCheckInTime());
            attendanceInfo.setDeleted(attendanceDO.isDeleted());
            attendanceInfo.setCheckoutTime(null); // 用户签到时还未签出，因此设置签出时间为 null
        }
        return attendanceInfo;
    }




    @Override
    //签退返回签到信息
    public AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq){
        AttendanceDO attendanceDO = attendanceMapper.userCheckOut(userCheckoutReq);
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

//    @Override
//    //查社团成员指定时间打卡记录
//    public List<AttendanceInfoVO> getAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq){
//        List<AttendanceInfoVO> attendanceInfoVOList = attendanceMapper.getAttendanceRecord(getAttendanceRecordReq);
//        for(AttendanceInfoVO attendanceInfoVO : attendanceInfoVOList){
//            String userName = userInfoIService.selectUserBasicInfo(attendanceInfoVO.getUserId()).getName();
//            attendanceInfoVO.setUserName(userName);
//        }
//        return attendanceInfoVOList;
//    }
//


    @Override
// 查社团所有成员指定时间打卡记录（分页）
    public PageVO<AttendanceInfoVO> getAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq) {
        // 构造分页对象
        Page<AttendanceDO> page = new Page<>(getAttendanceRecordReq.getCurrentPage(), getAttendanceRecordReq.getPageSize(), true);
        // 构建查询条件
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("club_id", getAttendanceRecordReq.getClubId()) // 等于条件
                .eq(getAttendanceRecordReq.getUserId() != null, "user_id", getAttendanceRecordReq.getUserId()) // 如果 userId 不为 null，则加入等于条件
                .between(getAttendanceRecordReq.getStartTime() != null
                                && getAttendanceRecordReq.getEndTime() != null, "checkin_time", getAttendanceRecordReq.getStartTime(),
                        getAttendanceRecordReq.getEndTime()) // 如果 startTime 和 endTime 都不为 null，则加入 BETWEEN 条件
                .orderByDesc("checkin_time"); // 按照 checkin_time 字段降序排列


        List<AttendanceDO> attendanceRecordList = attendanceMapper.selectList(page, queryWrapper);

        List<AttendanceInfoVO> result = new ArrayList<>();
        // DO转为VO展示给前端
        for (AttendanceDO attendanceDO : attendanceRecordList) {
            AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();

            attendanceInfoVO.setId(attendanceDO.getId());
            attendanceInfoVO.setClubId(attendanceDO.getClubId());
            attendanceInfoVO.setUserId(attendanceDO.getUserId());
            String userName = userInfoIService.selectUserBasicInfo(attendanceInfoVO.getUserId()).getName();
            attendanceInfoVO.setUserName(userName);
            attendanceInfoVO.setCheckInTime(attendanceDO.getCheckInTime());
            attendanceInfoVO.setCheckoutTime(attendanceDO.getCheckoutTime());
            attendanceInfoVO.setDeleted(attendanceDO.isDeleted());

            result.add(attendanceInfoVO);
        }

        // 封装分页查询结果并返回
        return new PageVO<>(result,page);

    }


    @Override
    public PageVO<AttendanceInfoVO> getAttendanceRecordT(GetAttendanceRecordReq getAttendanceRecordReq) {

        Page<AttendanceDO> page = attendanceMapper.findAttendanceInfoVOPage(getAttendanceRecordReq);
        List<AttendanceInfoVO> result = new ArrayList<>();
        for (AttendanceDO attendanceDO : page.getRecords()) {
            AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();
            attendanceInfoVO.setId(attendanceDO.getId());
            attendanceInfoVO.setClubId(attendanceDO.getClubId());
            attendanceInfoVO.setUserId(attendanceDO.getUserId());
            String userName = userInfoIService.selectUserBasicInfo(attendanceInfoVO.getUserId()).getName();
            attendanceInfoVO.setUserName(userName);
            attendanceInfoVO.setCheckInTime(attendanceDO.getCheckInTime());
            attendanceInfoVO.setCheckoutTime(attendanceDO.getCheckoutTime());
            attendanceInfoVO.setDeleted(attendanceDO.isDeleted());
            result.add(attendanceInfoVO);
        }
        return new PageVO<>(result, page);
    }


    //社团成员申请补签
    public Integer userReplenishAttendance(ApplyAttendanceReq applyAttendanceReq){
        System.out.println(applyAttendanceReq.getCheckInTime().toLocalDate());
        //签到签退时间要在同一天
        if(applyAttendanceReq.getCheckInTime().toLocalDate() != applyAttendanceReq.getCheckoutTime().toLocalDate()){
            return 0;
        }
        Integer rowsAffected = attendanceMapper.userReplenishAttendance(applyAttendanceReq);
        return rowsAffected;
    }



    //定时逻辑删除签到记录
    @Override
    public int timedDeleteRecord() {
            return attendanceMapper.timedDeleteRecord();
    }
}



