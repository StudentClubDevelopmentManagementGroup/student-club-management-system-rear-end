package team.project.module.club.attendance.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.*;
import team.project.module.club.attendance.internal.model.view.AttendanceInfoVO;
import team.project.module.club.attendance.internal.model.view.ClubAttendanceDurationVO;
import java.util.List;


//数据库持久化层
//如果使用Mybatis-plus提供的方法不需要再写.xml映射文件
//复杂的数据库操作需要写SQL

//SQL 放 mapper 层
@Mapper
public interface AttendanceMapper extends BaseMapper<AttendanceDO> {




    //签到返回签到信息
    default AttendanceInfoVO userCheckIn(UserCheckInReq userCheckinReq) {


        // 插入签到信息到数据库中
        AttendanceDO attendanceDO = new AttendanceDO();

        attendanceDO.setUserId(userCheckinReq.getUserId());
        attendanceDO.setClubId(userCheckinReq.getClubId());
        attendanceDO.setCheckInTime(userCheckinReq.getCheckInTime());

        int insertSuccess = this.insert(attendanceDO); // 使用 MyBatis-Plus 提供的 save 方法插入数据

        // 创建一个 AttendanceInfoVO 对象，用于存储签到信息
        AttendanceInfoVO attendanceInfo = new AttendanceInfoVO();
        // 如果插入成功，则设置签到信息的ID属性
        if (insertSuccess>0) {
            attendanceInfo.setId(attendanceDO.getId());
            attendanceInfo.setUserId(userCheckinReq.getUserId());
            attendanceInfo.setClubId(userCheckinReq.getClubId());
            attendanceInfo.setCheckInTime(userCheckinReq.getCheckInTime());
            attendanceInfo.setDeleted(attendanceDO.isDeleted());
            attendanceInfo.setCheckoutTime(null); // 用户签到时还未签出，因此设置签出时间为 null
        }
        // 返回签到信息
        return attendanceInfo;
    }


    //签退,返回完整信息
    default AttendanceInfoVO userCheckOutTest(UserCheckoutReq userCheckoutReq){
        AttendanceInfoVO latestCheckInRecord =
                getLatestCheckInRecord(userCheckoutReq.getUserId(), userCheckoutReq.getClubId());
        if (latestCheckInRecord != null && latestCheckInRecord.getCheckoutTime() == null){
            // 补全签退字段
            latestCheckInRecord.setCheckoutTime(userCheckoutReq.getCheckoutTime());
            // 更新数据库中的签到记录的签退时间字段
            AttendanceDO attendanceDO = new AttendanceDO();
            BeanUtils.copyProperties(latestCheckInRecord, attendanceDO); // 将视图对象的属性复制到数据对象中
            updateById(attendanceDO); // 更新数据库中的记录
            // 返回签到信息
            return latestCheckInRecord;

        }else{
            return null;
        }

    }


    //查询社团成员当天最新的签到记录
    default AttendanceInfoVO getLatestCheckInRecord(String userId, Long clubId) {
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("club_id", clubId)
                .eq("is_deleted", 0)
                .apply("DATE(checkin_time) = CURDATE()")
                .orderByDesc("checkin_time")
                .last("LIMIT 1");
        AttendanceDO attendanceDO = this.selectOne(queryWrapper);
        AttendanceInfoVO attendanceInfoVO = new AttendanceInfoVO();
        BeanUtils.copyProperties(attendanceDO, attendanceInfoVO);
        return attendanceInfoVO;
    }




    //查询社团成员当天的签到记录
    default List<AttendanceDO> getDayCheckIn(DayCheckInReq dayCheckInReq) {
        // 构造查询条件，只匹配指定日期的范围
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dayCheckInReq.getUserId())
                .eq("club_id", dayCheckInReq.getClubId())
                .eq("DATE(checkin_time)", dayCheckInReq.getDate());

        // 调用 MyBatis-Plus 提供的查询方法进行查询
        List<AttendanceDO> attendanceDOList = this.selectList(queryWrapper);
        return attendanceDOList;
    }



    //查社团一个成员一周打卡时长
    Long getTotalWeekSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId

    );
    //查社团一个成员一个月打卡时长
    Long getTotalMonthSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId,
            @Param("year") int year,
            @Param("month") int month
    );
    //查社团一个成员一年打卡时长
    Long getTotalYearSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId,
            @Param("year") int year111
    );


    //查社团一个成员指定时间打卡时长
    Long getAnyDurationSecondsT(GetOneAnyDurationReq getOneAnyDurationReq);


    //查询社团每个成员每个月打卡时长
    List<ClubAttendanceDurationVO> getEachTotalMonthDuration(
            @Param("clubId") Long clubId,
            @Param("year") int year,
            @Param("month") int month

    );

    //查询社团每个成员每年打卡时长
    List<ClubAttendanceDurationVO> getEachTotalYearDuration(
            @Param("clubId") Long clubId,
            @Param("year") int year

    );
    //查询社团每个成员本周打卡时长
    List<ClubAttendanceDurationVO> getEachTotalWeekDuration(@Param("clubId") Long clubId);


    //查询社团每个成员指定时间段打卡时长
    List<ClubAttendanceDurationVO> getEachTotalAnyDuration(GetEachAnyDurationReq getEachAnyDurationReq);

}





