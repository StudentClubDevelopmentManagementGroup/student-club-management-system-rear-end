package team.project.module.club.attendance.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.module.club.attendance.internal.model.entity.AttendanceDO;
import team.project.module.club.attendance.internal.model.request.DayCheckInReq;

import java.util.List;

//如果使用Mybatis-plus提供的方法不需要再写.xml映射文件
//复杂的数据库操作需要写SQL
@Mapper
public interface AttendanceMapper extends BaseMapper<AttendanceDO> {

    default List<AttendanceDO> getDayCheckIn(DayCheckInReq dayCheckInReq) {
        // 构造查询条件，只匹配指定日期的范围

        /* TODO SQL 放 mapper 层 */
        QueryWrapper<AttendanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dayCheckInReq.getUserId())
                .eq("club_id", dayCheckInReq.getClubId())
                .eq("DATE(checkin_time)", dayCheckInReq.getDate());

        // 调用 MyBatis-Plus 提供的查询方法进行查询
        List<AttendanceDO> attendanceDOList = this.selectList(queryWrapper);
        return attendanceDOList;
    }

    Long getTotalMonthSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId,
            @Param("year") int year,
            @Param("month") int month
    );

    Long getTotalYearSeconds(
            @Param("userId") String userId,
            @Param("clubId") Long clubId,
            @Param("year") int year
    );




}





