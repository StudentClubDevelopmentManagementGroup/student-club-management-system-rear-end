package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.mapper.TblUserClubMapper;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p style="color: #f23215;">
 * 基地表 服务实现类
 * </p>
 *
 * @author LIn
 * @since 2024-03-17
 */
@Service
public class TblUserClubServiceImpl extends ServiceImpl<TblUserClubMapper, TblUserClubDO> implements TblUserClubService {
    @Autowired
    TblUserClubMapper ucMapper;  /* 示例 */

    public void setClubManager(Long userId, Long clubId) {
        Map<String, Object> userClubMap = new HashMap<>();
        userClubMap.put("userId", userId);
        userClubMap.put("clubId", clubId);
        List<TblUserClubDO> userList = ucMapper.selectByMap(userClubMap);
        if(userList.isEmpty()){
            ucMapper.createManager(userId, clubId);
        }
        else {
            ucMapper.setManager(userId, clubId);
        }
    }


}
