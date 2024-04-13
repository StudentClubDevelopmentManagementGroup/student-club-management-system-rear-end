package team.project.module.community.display.service;

import team.project.module.community.display.model.request.UserCheckinReq;

public interface AttendenceService {
    public boolean userCheckIn(UserCheckinReq userCheckinReq);
}
