package team.project.module.auth.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.service.AuthServiceI;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;

@Service
public class AuthServiceImpl implements AuthServiceI {

    @Autowired
    UserInfoServiceI userInfoService;

    @Autowired
    PceIService clubMemberRoleService;

    /**
     * 详见：{@link AuthServiceI#requireClubManager}
     * */
    @Override
    public void requireClubManager(String userId, long clubId, String message) {
        Integer roleCode;
        if (   null == userId
          || ! clubMemberRoleService.isClubManager(userId, clubId)
          ||   null == (roleCode = userInfoService.selectUserRole(userId))
          || ! UserRole.hasRole(roleCode, UserRole.SUPER_ADMIN)
        ) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, message);
        }
    }
}
