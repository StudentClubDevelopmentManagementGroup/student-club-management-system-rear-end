package team.project.module.community.display.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import team.project.module.community.display.mapper.AttendenceMapper;
import team.project.module.community.display.model.entity.Attendence;
import team.project.module.community.display.model.request.UserCheckinReq;
import team.project.module.community.display.service.AttendenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class AttendenceServiceImpl extends ServiceImpl<AttendenceMapper, Attendence> implements AttendenceService {

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
}
