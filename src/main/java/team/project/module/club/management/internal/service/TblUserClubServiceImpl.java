package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.mapper.TblUserClubMapper;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.user.export.service.UserInfoIService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static team.project.module.user.export.model.enums.UserRole.CLUB_MANAGER;


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
    TblUserClubMapper ucMapper;
    @Autowired
    UserInfoIService uiService;
    public void setClubManager(String userId, Long clubId) {
        Map<String, Object> userClubMap = new HashMap<>();
        userClubMap.put("user_id", userId);
        userClubMap.put("club_id", clubId);
        List<TblUserClubDO> userList = ucMapper.selectByMap(userClubMap);
        if(userList.isEmpty()){
            ucMapper.createManager(userId, clubId);
        }
        else {
            ucMapper.setManager(userId, clubId);
            //
            uiService.addRoleToUser(userId, CLUB_MANAGER);
        }
    }

    @Override
    public void quashClubManager(String userId, Long clubId) {
        Map<String, Object> userClubMap = new HashMap<>();
        userClubMap.put("user_id", userId);
        userClubMap.put("club_id", clubId);
        List<TblUserClubDO> userList = ucMapper.selectByMap(userClubMap);
        if(userList.isEmpty()){
            throw new ServiceException(ServiceStatus.SUCCESS, "没有对象");
        }
        else {
            ucMapper.quashManager(userId, clubId);
            //
            uiService.removeRoleFromUser(userId, CLUB_MANAGER);
        }

    }


}
