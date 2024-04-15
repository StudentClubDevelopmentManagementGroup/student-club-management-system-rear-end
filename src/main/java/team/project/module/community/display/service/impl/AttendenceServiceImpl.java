package team.project.module.community.display.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.community.display.mapper.AttendenceMapper;
import team.project.module.community.display.model.entity.Attendence;
import team.project.module.community.display.model.request.attendence.DayCheckInReq;
import team.project.module.community.display.model.request.attendence.UserCheckinReq;
import team.project.module.community.display.model.request.attendence.UserCheckoutReq;
import team.project.module.community.display.service.AttendenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendenceServiceImpl extends ServiceImpl<AttendenceMapper, Attendence> implements AttendenceService {

    @Autowired
    private AttendenceMapper attendenceMapper;
    //社团成员签到
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

    //查询社团成员当天签到记录
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
    // 社团成员签退
    @Override
    public boolean userCheckOut(UserCheckoutReq userCheckoutReq) {
        // 根据记录ID查询数据库中的签到记录
        Attendence attendence = attendenceMapper.selectById(userCheckoutReq.getId());

        // 如果找到了对应的记录
        if (attendence != null) {
            // 更新记录的签退时间
            attendence.setCheckoutTime(userCheckoutReq.getCheckoutTime()); // 签退时间为前端发过来的时间
            //修改了对象的属性，这个修改只会在内存中生效，不会自动反映到数据库中对应的记录上。
            // 调用 MyBatis-Plus 提供的更新方法，更新记录到数据库中
            System.out.println(userCheckoutReq.getCheckoutTime()+userCheckoutReq.getUserId()+userCheckoutReq.getClubId());
            boolean success = this.updateById(attendence);
            // 返回更新操作是否成功的结果
            return success;
        } else {
            // 如果未找到对应的记录，则返回失败
            return false;
        }
    }


}
