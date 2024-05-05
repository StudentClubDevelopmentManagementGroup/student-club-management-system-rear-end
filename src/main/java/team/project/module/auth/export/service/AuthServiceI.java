package team.project.module.auth.export.service;

import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

public interface AuthServiceI {

    boolean isSuperAdmin(String userId);

    boolean isClubManager(String userId, long clubId);

    default void requireClubManager(String userId, long clubId, String message) {
        if (userId == null || ! isClubManager(userId, clubId) || ! isSuperAdmin(userId)) {
            throw new ServiceException(ServiceStatus.FORBIDDEN, message);
        }
    }
}
