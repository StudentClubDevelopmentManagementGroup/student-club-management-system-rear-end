package team.project.module.community.display.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.community.display.mapper.AttendenceMapper;
import team.project.module.community.display.model.entity.Attendence;
import team.project.module.community.display.model.request.attendence.DayCheckInReq;
import team.project.module.community.display.model.request.attendence.UserCheckinReq;
import team.project.module.community.display.service.AttendenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendenceServiceImpl extends ServiceImpl<AttendenceMapper, Attendence> implements AttendenceService {

    @Autowired
    private AttendenceMapper attendenceMapper;
    @Override
    public boolean userCheckIn(UserCheckinReq userCheckinReq) {
        // 将 UserCheckinReq 对象转换为 Attendence 对象
        Attendence attendence = new Attendence();
        BeanUtils.copyProperties(userCheckinReq, attendence); // 将属性复制到 Attendence 对象中

        // 调用 MyBatis-Plus 提供的 save 方法将签到记录插入数据库
        boolean success = this.save(attendence);

        // 如果插入成功，则返回 true；否则返回 false
        return success;
    }

    @Override
    public List<Attendence> getDayCheckIn(DayCheckInReq dayCheckInReq) {
        // 构造查询条件，只匹配指定日期的范围


        QueryWrapper<Attendence> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dayCheckInReq.getUserId())
                .eq("club_id", dayCheckInReq.getClubId())
                .eq("DATE(checkin_time)", dayCheckInReq.getDate());;

        System.out.println("DATE(checkin_time)");
        // 调用 MyBatis-Plus 提供的查询方法进行查询
        List<Attendence> attendenceList = attendenceMapper.selectList(queryWrapper);
        return attendenceList;
    }
}
