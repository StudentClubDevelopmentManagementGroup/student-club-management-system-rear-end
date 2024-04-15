package team.project.module.club.attendance.internal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.attendance.internal.mapper.AttendanceMapper;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.DayCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckInReq;
import team.project.module.club.attendance.internal.model.request.UserCheckoutReq;
import team.project.module.club.attendance.internal.service.AttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, AttendanceDO> implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;
    //社团成员签到
    @Override
    public boolean userCheckIn(UserCheckInReq userCheckinReq) {
        // 将 UserCheckInReq 对象转换为 Attendance 对象
        AttendanceDO attendanceDO = new AttendanceDO();
        BeanUtils.copyProperties(userCheckinReq, attendanceDO); // 将属性复制到 Attendance 对象中

        // 调用 MyBatis-Plus 提供的 save 方法将签到记录插入数据库
        boolean success = this.save(attendanceDO);

        // 如果插入成功，则返回 true；否则返回 false
        return success;
    }

    //查询社团成员当天签到记录
    @Override
    public List<AttendanceDO> getDayCheckIn(DayCheckInReq dayCheckInReq) {
        // 构造查询条件，只匹配指定日期的范围

        /* TODO SQL 放 mapper 层 */
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dayCheckInReq.getUserId())
                .eq("club_id", dayCheckInReq.getClubId())
                .eq("DATE(checkin_time)", dayCheckInReq.getDate());
        // 调用 MyBatis-Plus 提供的查询方法进行查询
        List<AttendanceDO> attendanceDOList = attendanceMapper.selectList(queryWrapper);
        return attendanceDOList;
    }
    // 社团成员签退
    @Override
    public boolean userCheckOut(UserCheckoutReq userCheckoutReq) {
        // 根据记录ID查询数据库中的签到记录
        AttendanceDO attendanceDO = attendanceMapper.selectById(userCheckoutReq.getId());

//        int result = attendanceMapper.update(new LambdaUpdateWrapper<AttendanceDO>()
//                .eq(AttendanceDO::getId, userCheckoutReq.getUserId())
//                .set(AttendanceDO::getCheckoutTime, userCheckoutReq.getCheckoutTime())
//        );

        // 如果找到了对应的记录
        if (attendanceDO != null) {
            // 更新记录的签退时间
            attendanceDO.setCheckoutTime(userCheckoutReq.getCheckoutTime()); // 签退时间为前端发过来的时间
            //修改了对象的属性，这个修改只会在内存中生效，不会自动反映到数据库中对应的记录上。
            // 调用 MyBatis-Plus 提供的更新方法，更新记录到数据库中
            boolean success = this.updateById(attendanceDO);
            // 返回更新操作是否成功的结果
            return success;
        } else {
            // 如果未找到对应的记录，则返回失败
            return false;
        }
    }
}