package team.project.module.club.attendance.internal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.management.export.model.datatransfer.ClubBasicMsgDTO;
import team.project.module.club.management.export.servivce.ManagementIService;
import team.project.module.user.export.service.UserInfoIService;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * 把 DO 对象转换为 VO
 */
@Component
public class ToolMethods {
    @Autowired
    private UserInfoIService userInfoIService;
    @Autowired
    public ManagementIService managementIService;
    public AttendanceInfoVO convert(AttendanceDO attendanceDO) {
        AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();

        ClubBasicMsgDTO clubBasicMsgDTO = managementIService.selectClubBasicMsg(attendanceDO.getClubId());
        String departmentName = clubBasicMsgDTO.getDepartmentName();
        attendanceInfoVO.setDepartmentName(departmentName);
        String clubName = clubBasicMsgDTO.getName();
        attendanceInfoVO.setClubName(clubName);

        attendanceInfoVO.setUserId(attendanceDO.getUserId());
        String userName = userInfoIService.selectUserBasicInfo(attendanceDO.getUserId()).getName();
        attendanceInfoVO.setUserName(userName);
        attendanceInfoVO.setCheckInTime(attendanceDO.getCheckInTime());
        attendanceInfoVO.setCheckoutTime(attendanceDO.getCheckoutTime());
        // 获取时长的总秒数
        long seconds = calculateDurationTime(attendanceDO.getCheckInTime(),attendanceDO.getCheckoutTime());
        attendanceInfoVO.setAttendanceDuration(seconds);
        attendanceInfoVO.setDeleted(attendanceDO.isDeleted());

        return attendanceInfoVO;
    }


    //如果输入了学号就只通过学号查询一次名字而已
    public AttendanceInfoVO convert(AttendanceDO attendanceDO,String userName) {
        AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();

        ClubBasicMsgDTO clubBasicMsgDTO = managementIService.selectClubBasicMsg(attendanceDO.getClubId());
        String departmentName = clubBasicMsgDTO.getDepartmentName();
        attendanceInfoVO.setDepartmentName(departmentName);
        String clubName = clubBasicMsgDTO.getName();
        attendanceInfoVO.setClubName(clubName);

        attendanceInfoVO.setUserId(attendanceDO.getUserId());
        attendanceInfoVO.setUserName(userName);
        attendanceInfoVO.setCheckInTime(attendanceDO.getCheckInTime());
        attendanceInfoVO.setCheckoutTime(attendanceDO.getCheckoutTime());
        // 获取时长的总秒数
        long seconds = calculateDurationTime(attendanceDO.getCheckInTime(),attendanceDO.getCheckoutTime());
        attendanceInfoVO.setAttendanceDuration(seconds);
        attendanceInfoVO.setDeleted(attendanceDO.isDeleted());

        return attendanceInfoVO;
    }




    public Long calculateDurationTime(LocalDateTime t1, LocalDateTime t2) {
        // 如果 t2 为 null，则使用当前时间作为 t2
        if (t2 == null) {
            t2 = LocalDateTime.now();
        }

        // 计算 t1 和 t2 之间的时长
        Duration duration = Duration.between(t1, t2);

        // 获取时长的总秒数
        long seconds = duration.getSeconds();

        // 将结果作为 Long 类型返回（如果需要处理可能很大的值）
        return seconds;
    }
}
