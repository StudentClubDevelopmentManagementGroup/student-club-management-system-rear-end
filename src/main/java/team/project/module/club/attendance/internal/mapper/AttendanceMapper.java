package team.project.module.club.attendance.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;

import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


//数据库持久化层
//如果使用Mybatis-plus提供的方法不需要再写.xml映射文件
//复杂的数据库操作需要在xml写SQL
//SQL 放 mapper 层
@Mapper
public interface AttendanceMapper extends BaseMapper<AttendanceDO> {


    //签到返回签到信息
    default AttendanceDO userCheckIn(UserCheckInReq userCheckinReq) {

        // 插入签到信息到数据库中
        AttendanceDO attendanceDO = new AttendanceDO();
        attendanceDO.setUserId(userCheckinReq.getUserId());
        attendanceDO.setClubId(userCheckinReq.getClubId());
        attendanceDO.setCheckInTime(userCheckinReq.getCheckInTime());

        int insertSuccess = this.insert(attendanceDO); // 使用 MyBatis-Plus 提供的 save 方法插入数据
        if(insertSuccess>0){
            // 获取刚刚插入的记录的主键值
            Long attendanceId = attendanceDO.getId(); // 假设 id 是自增主键
            // 根据主键查询刚刚插入的记录
            AttendanceDO attendanceDO1 = this.selectById(attendanceId); // 使用 MyBatis-Plus 提供的 getById 方法查询数据
            return attendanceDO1;
        }else return null;
        // 返回签到信息

    }




    //签退,返回完整信息
    default AttendanceDO userCheckOut(UserCheckoutReq userCheckoutReq){
        //先查询今天最新的签到记录
        AttendanceDO latestCheckInRecord =
                getLatestCheckInRecord(userCheckoutReq.getUserId(), userCheckoutReq.getClubId());
        if(latestCheckInRecord !=null && latestCheckInRecord.getCheckoutTime() == null){
            // 补全签退字段
            latestCheckInRecord.setCheckoutTime(userCheckoutReq.getCheckoutTime());
            // 更新数据库中的签到记录的签退时间字段
            updateById(latestCheckInRecord); // 更新数据库中的记录
            // 返回签到信息
            return latestCheckInRecord;
        }else {
            return null;
        }
    }


    //查询社团成员当天最新的签到记录
    default AttendanceDO getLatestCheckInRecord(String userId, Long clubId) {
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("club_id", clubId)
                .eq("is_deleted", 0)
                .apply("DATE(checkin_time) = CURDATE()")
                .orderByDesc("checkin_time")
                .last("LIMIT 1");
        AttendanceDO attendanceDO = this.selectOne(queryWrapper);

        return attendanceDO;

    }

    //返回分页查询对象
    default Page<AttendanceDO> findAttendanceInfoVOPage(GetAttendanceRecordReq getAttendanceRecordReq){
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

        return this.selectPage(page, queryWrapper);
    }


    //查社团一个成员指定时间打卡时长
    Long getOneAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq);


    //查询社团每个成员指定时间段打卡时长
    List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq);





    //社团成员申请补签
    default Integer userReplenishAttendance(ApplyAttendanceReq applyAttendanceReq){

        //创建更新条件
        UpdateWrapper<AttendanceDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", applyAttendanceReq.getUserId())
                .eq("club_id", applyAttendanceReq.getClubId())
                .eq("checkin_time",applyAttendanceReq.getCheckInTime())
                //签到时间要在七天内
                .ge("checkin_time", LocalDateTime.now().minus(7, ChronoUnit.DAYS))
                .eq("is_deleted", true)
                .isNull("checkout_time");

        //创建要更新的字段
        AttendanceDO attendanceDO = new AttendanceDO();
        attendanceDO.setCheckoutTime(applyAttendanceReq.getCheckoutTime());
        attendanceDO.setDeleted(false);

        return this.update(attendanceDO,updateWrapper);

    }


    //定时逻辑删除记录
    @Update("UPDATE tbl_user_club_attendance SET is_deleted = 1 " +
            "WHERE checkout_time IS NULL " +
            "AND is_deleted = 0 " +
            "AND DATE(checkin_time) = CURDATE()")
    int timedDeleteRecord();




}





