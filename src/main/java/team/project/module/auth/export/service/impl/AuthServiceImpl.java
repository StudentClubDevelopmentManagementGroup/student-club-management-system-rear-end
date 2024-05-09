package team.project.module.auth.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.auth.export.exception.AuthenticationFailureException;
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

    private boolean isSuperAdmin(String userId) {
        Integer roleCode = userInfoService.selectUserRole(userId);
        return null != roleCode && UserRole.hasRole(roleCode, UserRole.SUPER_ADMIN);
    }

    /**
     * 详见：{@link AuthServiceI#requireClubManager}
     * */
    @Override
    public void requireClubManager(String userId, long clubId, String message) {

        if (null == userId)
            throw new AuthenticationFailureException(message);

        if (clubMemberRoleService.isClubManager(userId, clubId))
            return;

        if (isSuperAdmin(userId))
            return;

        throw new AuthenticationFailureException(message);
    }

    /**
     * 详见：{@link AuthServiceI#requireClubMember}
     */
    @Override
    public void requireClubMember(String userId, long clubId, String message) {

        if (null == userId)
            throw new AuthenticationFailureException(message);

        if (clubMemberRoleService.isClubMember(userId, clubId))
            return;

        if (isSuperAdmin(userId))
            return;

        throw new AuthenticationFailureException(message);
    }
}
