package team.project.module.club.attendance.internal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.user.export.service.UserInfoIService;

/**
 *
 * 把 DO 对象转换为 VO
 */
@Component
public class ConvertToAttendanceInfoVO {
    @Autowired
    private UserInfoIService userInfoIService;
    public AttendanceInfoVO convert(AttendanceDO attendanceDO) {
        AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();

        attendanceInfoVO.setId(attendanceDO.getId());
        attendanceInfoVO.setClubId(attendanceDO.getClubId());
        attendanceInfoVO.setUserId(attendanceDO.getUserId());
        String userName = userInfoIService.selectUserBasicInfo(attendanceDO.getUserId()).getName();
        attendanceInfoVO.setUserName(userName);
        attendanceInfoVO.setCheckInTime(attendanceDO.getCheckInTime());
        attendanceInfoVO.setCheckoutTime(attendanceDO.getCheckoutTime());
        attendanceInfoVO.setDeleted(attendanceDO.isDeleted());

        return attendanceInfoVO;
    }
}
