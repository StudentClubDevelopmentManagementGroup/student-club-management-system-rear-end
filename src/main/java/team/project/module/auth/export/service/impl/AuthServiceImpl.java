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

    /**
     * 详见：{@link AuthServiceI#requireClubManager}
     * */
    @Override
    public void requireClubManager(String userId, long clubId, String message) {

        if (null == userId)
            throw new AuthenticationFailureException(message);

        if (clubMemberRoleService.isClubManager(userId, clubId))
            return;

        Integer userRole = userInfoService.selectUserRole(userId);
        if (null != userRole && UserRole.hasRole(userRole, UserRole.SUPER_ADMIN))
            return;

        throw new AuthenticationFailureException(message);
    }

    /**
     * 详见：{@link AuthServiceI#requireClubTeacherManager}
     * */
    @Override
    public void requireClubTeacherManager(String userId, long clubId, String message) {

        if (null == userId)
            throw new AuthenticationFailureException(message);

        Integer userRole = userInfoService.selectUserRole(userId);
        if (null == userRole)
            throw new AuthenticationFailureException(message);

        if (UserRole.hasRole(userRole, UserRole.SUPER_ADMIN))
            return;

        if ( ! UserRole.hasRole(userRole, UserRole.TEACHER))
            throw new AuthenticationFailureException(message);

        if (clubMemberRoleService.isClubManager(userId, clubId))
            return;

        throw new AuthenticationFailureException(message);
    }

    /**
     * 详见：{@link AuthServiceI#requireSuperAdmin}
     */
    @Override
    public void requireSuperAdmin(String userId, String message) {
        if (null == userId)
            throw new AuthenticationFailureException(message);

        Integer userRole = userInfoService.selectUserRole(userId);
        if (null != userRole && UserRole.hasRole(userRole, UserRole.SUPER_ADMIN))
            return;

        throw new AuthenticationFailureException(message);
    }
}
