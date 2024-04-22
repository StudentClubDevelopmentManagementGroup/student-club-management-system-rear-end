package team.project.module.club.attendance.internal.service.impl;

import org.apache.ibatis.annotations.Param;
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
import java.util.List;



@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, AttendanceDO> implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;



    @Override
    //签到返回签到信息
    public AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq){
        return attendanceMapper.userCheckIn(userCheckinReq);

    }
    //查询社团成员当天最新的签到记录
    @Override
    public AttendanceInfoVO getLatestCheckInRecord(String userId, Long clubId){
        return attendanceMapper.getLatestCheckInRecord(userId,clubId);

    }



    //查当天签到记录测试
    @Override
    public List<AttendanceDO> getDayCheckInTest(DayCheckInReq dayCheckInReq){
        return attendanceMapper.getDayCheckIn(dayCheckInReq);

    }
    @Override
    //签退返回签到信息
    public AttendanceInfoVO userCheckOutTest(UserCheckoutReq userCheckoutReq){
        return attendanceMapper.userCheckOutTest(userCheckoutReq);
    }



    //补签
    @Override
    public AttendanceInfoVO makeUpAttendance(ApplyAttendanceReq applyAttendanceReq) {
        // 将 ApplyAttendanceReq 对象转换为 AttendanceDO 对象
        AttendanceDO attendanceDO = new AttendanceDO();
        BeanUtils.copyProperties(applyAttendanceReq, attendanceDO); // 将属性复制到 AttendanceDO 对象中
        // 调用 MyBatis-Plus 提供的 save 方法将补签记录插入数据库
        boolean success = this.save(attendanceDO);

        // 创建一个用于返回的对象
        AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();

        // 如果保存成功，则将数据库中生成的 ID 和其他信息填充到返回对象中
        if (success) {
            attendanceInfoVO.setId(attendanceDO.getId());
            attendanceInfoVO.setUserId(attendanceDO.getUserId());
            attendanceInfoVO.setClubId(attendanceDO.getClubId());
            attendanceInfoVO.setCheckInTime(attendanceDO.getCheckInTime());
            attendanceInfoVO.setCheckoutTime(attendanceDO.getCheckoutTime());
            attendanceInfoVO.setDeleted(attendanceDO.isDeleted());
        }
        // 返回包含插入数据的对象
        return attendanceInfoVO;
    }





    //查询社团成员一周的打卡时长
    @Override
    public Long getTotalWeekSeconds(String userId, Long clubId) {
        return attendanceMapper.getTotalWeekSeconds(userId, clubId);
    }

    //查询社团成员一个月的打卡时长
    @Override
    public Long getTotalMonthSeconds(String userId, Long clubId, int year, int month) {
        return attendanceMapper.getTotalMonthSeconds(userId, clubId, year, month);
    }


    //查询社团成员一年的打卡时长
    @Override
    public Long getTotalYearSeconds(String userId, Long clubId, int year) {
        return attendanceMapper.getTotalYearSeconds(userId, clubId, year);
    }



    //查社团一个成员指定时间打卡时长
    public Long getAnyDurationSecondsT(GetOneAnyDurationReq getOneAnyDurationReq){
        return attendanceMapper.getAnyDurationSecondsT(getOneAnyDurationReq);
    }

    //查询社团每个成员每个月的打卡时长
    @Override
    public List<ClubAttendanceDurationVO> getEachTotalMonthDuration(Long clubId, int year, int month) {
        return attendanceMapper.getEachTotalMonthDuration(clubId,year,month);
    }

    @Override
    //查询社团每个成员每年打卡时长
    public List<ClubAttendanceDurationVO> getEachTotalYearDuration(Long clubId, int year) {
        return attendanceMapper.getEachTotalYearDuration(clubId,year);
    }
    @Override
    //查询社团每个成员本周打卡时长
    public List<ClubAttendanceDurationVO> getEachTotalWeekDuration(@Param("clubId") Long clubId){
        return attendanceMapper.getEachTotalWeekDuration(clubId);
    }


    //查询社团每个成员指定时间段打卡时长
    public List<ClubAttendanceDurationVO> getEachTotalAnyDuration(GetEachAnyDurationReq getEachAnyDurationReq){
        return attendanceMapper.getEachTotalAnyDuration(getEachAnyDurationReq);
    }
}



