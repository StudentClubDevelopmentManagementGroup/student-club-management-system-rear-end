package team.project.module.club.attendance.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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


    default AttendanceDO userCheckIn(String userId, Long clubId, LocalDateTime checkInTime) {
        AttendanceDO attendanceDO = new AttendanceDO();
        attendanceDO.setUserId(userId);
        attendanceDO.setClubId(clubId);
        attendanceDO.setCheckInTime(checkInTime);

        int insertSuccess = this.insert(attendanceDO);
        if(insertSuccess > 0){
            return this.selectById(attendanceDO.getId());
        }
        return null;
    }
    default AttendanceDO userCheckOut(String userId, Long clubId, LocalDateTime checkoutTime) {
        AttendanceDO latestCheckInRecord = getLatestCheckInRecord(userId, clubId);
        if(latestCheckInRecord != null && latestCheckInRecord.getCheckoutTime() == null){
            latestCheckInRecord.setCheckoutTime(checkoutTime);
            updateById(latestCheckInRecord);
            return latestCheckInRecord;
        }
        return null;
    }


    //查询社团成员当天最新的签到记录
    default AttendanceDO getLatestCheckInRecord(String userId, Long clubId) {
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("club_id", clubId)
                .eq("is_deleted", 0)
                .apply("DATE(checkin_time) = CURDATE()") //获取今天的日期
                .orderByDesc("checkin_time")
                .last("LIMIT 1");
        AttendanceDO attendanceDO = this.selectOne(queryWrapper);

        return attendanceDO;

    }

    //查询未签退记录

    default List<AttendanceDO>  getUnCheckOutRecord(String userId, Long clubId) {
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("club_id", clubId)
                .eq("is_deleted", 1)
                .orderByDesc("checkin_time");
        // 使用 selectList 查询多个记录
        List<AttendanceDO> attendanceDOList = this.selectList(queryWrapper);

        return attendanceDOList;

    }


    default Page<AttendanceDO> getUnCheckOutRecordTest(String userId, Long clubId, int currentPage, int pageSize) {
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("club_id", clubId)
                .eq("is_deleted", 1)
                .orderByDesc("checkin_time");

        // 创建分页对象
        Page<AttendanceDO> page = new Page<>(currentPage, pageSize);

        // 使用 selectPage 进行分页查询
        Page<AttendanceDO> attendancePage = this.selectPage(page, queryWrapper);

        return attendancePage; // 返回分页结果
    }






    //查签到记录，返回分页查询对象，
    default Page<AttendanceDO> findAttendanceInfoVOPage(GetAttendanceRecordReq getAttendanceRecordReq,Long clubId){
        // 构造分页对象
        Page<AttendanceDO> page = new Page<>(getAttendanceRecordReq.getCurrentPage(), getAttendanceRecordReq.getPageSize(), true);
        // 构建查询条件

        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("club_id", clubId)
                .like(getAttendanceRecordReq.getUserId() != "", "user_id", "%" + getAttendanceRecordReq.getUserId() + "%")
                .between(getAttendanceRecordReq.getStartTime() != null && getAttendanceRecordReq.getEndTime() != null,
                        "checkin_time", getAttendanceRecordReq.getStartTime(),
                        getAttendanceRecordReq.getEndTime()) // 如果 startTime 和 endTime 都不为 null，则加入 BETWEEN 条件
                .orderByDesc("checkin_time"); // 按照 checkin_time 字段降序排列

        return this.selectPage(page, queryWrapper);
    }



    //查签到记录，返回分页查询对象,可以使用名字查询
    default Page<AttendanceDO> findAttendanceInfoVOPageTest(GetAttendanceRecordReq getAttendanceRecordReq,Long clubId,List<String> userIds){


            // 构造分页对象
            Page<AttendanceDO> page = new Page<>(getAttendanceRecordReq.getCurrentPage(), getAttendanceRecordReq.getPageSize(), true);
            // 构建查询条件

            QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("club_id", clubId)
//                .like(StringUtils.isNotBlank(getAttendanceRecordReq.getUserId()), "user_id", "%" + getAttendanceRecordReq.getUserId() + "%")
                    .like(getAttendanceRecordReq.getUserId() != "", "user_id", "%" + getAttendanceRecordReq.getUserId() + "%")
                    .in( "user_id", userIds)
                    .between(getAttendanceRecordReq.getStartTime() != null && getAttendanceRecordReq.getEndTime() != null,
                            "checkin_time", getAttendanceRecordReq.getStartTime(),
                            getAttendanceRecordReq.getEndTime()) // 如果 startTime 和 endTime 都不为 null，则加入 BETWEEN 条件
                    .orderByDesc("checkin_time"); // 按照 checkin_time 字段降序排列

            return this.selectPage(page, queryWrapper);


    }

    //用学号查询
    default Page<AttendanceDO> findAttendanceInfoVOPageTest(GetAttendanceRecordReq getAttendanceRecordReq){


        // 构造分页对象
        Page<AttendanceDO> page = new Page<>(getAttendanceRecordReq.getCurrentPage(), getAttendanceRecordReq.getPageSize(), true);
        // 构建查询条件

        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("club_id", getAttendanceRecordReq.getClubId())
//                .like(StringUtils.isNotBlank(getAttendanceRecordReq.getUserId()), "user_id", "%" + getAttendanceRecordReq.getUserId() + "%")
                .like(getAttendanceRecordReq.getUserId() != "", "user_id", "%" + getAttendanceRecordReq.getUserId() + "%")
                .between(getAttendanceRecordReq.getStartTime() != null && getAttendanceRecordReq.getEndTime() != null,
                        "checkin_time", getAttendanceRecordReq.getStartTime(),
                        getAttendanceRecordReq.getEndTime()) // 如果 startTime 和 endTime 都不为 null，则加入 BETWEEN 条件
                .orderByDesc("checkin_time"); // 按照 checkin_time 字段降序排列

        return this.selectPage(page, queryWrapper);

    }



    //查社团一个成员指定时间打卡时长
    Long getOneAttendanceDurationTime(GetAttendanceTimeReq getAttendanceTimeReq);


    //查询社团每个成员指定时间段打卡时长
    List<ClubAttendanceDurationVO> getEachAttendanceDurationTime(
            @Param("getAttendanceTimeReq") GetAttendanceTimeReq getAttendanceTimeReq,
            @Param("clubId") Long clubId
    );



    //查询社团每个成员指定时间段打卡时长
    List<ClubAttendanceDurationVO> getEachAttendanceDurationTimeByName(
            @Param("getAttendanceTimeReq") GetAttendanceTimeReq getAttendanceTimeReq,
            @Param("clubId") Long clubId,
            @Param("userIds") List<String> userIds
    );

    //社团成员申请补签
    default AttendanceDO userReplenishAttendance(ApplyAttendanceReq applyAttendanceReq, Long clubId){

        //创建更新条件
        UpdateWrapper<AttendanceDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", applyAttendanceReq.getUserId())
                .eq("club_id", clubId)
                .eq("checkin_time",applyAttendanceReq.getCheckInTime())
                //七天之内未签退的记录才能补签
                .ge("checkin_time", LocalDateTime.now().minus(7, ChronoUnit.DAYS))
                .eq("is_deleted", true)
                .isNull("checkout_time");
        //创建要更新的字段
        AttendanceDO attendanceDO = this.selectOne(updateWrapper);
        if (attendanceDO != null){
            attendanceDO.setCheckoutTime(applyAttendanceReq.getCheckoutTime());
            attendanceDO.setDeleted(false);
            int rowsAffected = this.update(attendanceDO,updateWrapper);
            //获取并返回刚刚更新的数据
            return attendanceDO;
        }
        return null;

    }




    //定时逻辑删除记录
    @Update("UPDATE tbl_user_club_attendance SET is_deleted = 1 " +
            "WHERE checkout_time IS NULL " +
            "AND is_deleted = 0 " +
            "AND DATE(checkin_time) = CURDATE()")
    int timedDeleteRecord();




}





