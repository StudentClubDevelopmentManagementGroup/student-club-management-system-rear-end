package team.project.module.auth.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public boolean isSuperAdmin(String userId) {
        Integer roleCode = userInfoService.selectUserRole(userId);
        return roleCode != null && UserRole.hasRole(roleCode, UserRole.SUPER_ADMIN);
    }

    @Override
    public boolean isClubManager(String userId, long clubId) {
        return clubMemberRoleService.isClubManager(userId, clubId);
    }
}
