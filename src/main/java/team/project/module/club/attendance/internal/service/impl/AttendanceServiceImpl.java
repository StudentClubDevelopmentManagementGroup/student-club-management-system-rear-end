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

    @Override
    //签退返回签到信息
    public AttendanceInfoVO userCheckOut(UserCheckoutReq userCheckoutReq){
        AttendanceDO attendanceDO = attendanceMapper.userCheckOutTest(userCheckoutReq);
        if(attendanceDO !=null){
            AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();
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
            AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();
            BeanUtils.copyProperties(attendanceMapper.getLatestCheckInRecord(userId,clubId), attendanceInfoVO);
            return attendanceInfoVO;
        }else{
            return null;
        }


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
    @Override
    //查社团一个成员指定时间打卡时长
    public Long getOneAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq){
        return attendanceMapper.getOneAttendanceDurationTime(getAttendanceTimeReq);
    }
    @Override
    //查询社团每个成员指定时间段打卡时长
    public List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq){
        return attendanceMapper.getEachAttendanceDurationTime(getAttendanceTimeReq);
    }
    @Override
    //查社团成员指定时间打卡记录
    public List<AttendanceInfoVO> getEachAttendanceRecord(GetAttendanceRecordReq getAttendanceRecordReq){
        return attendanceMapper.getAttendanceRecord(getAttendanceRecordReq);
    }





    //定时逻辑删除签到记录
    @Override
    public int timedDeleteRecord() {
            return attendanceMapper.timedDeleteRecord();
    }
}



