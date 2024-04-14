package team.project.module.community.display.service;

import team.project.module.community.display.model.entity.Attendence;
import team.project.module.community.display.model.request.attendence.DayCheckInReq;
import team.project.module.community.display.model.request.attendence.UserCheckinReq;

import java.time.LocalDate;
import java.util.List;

public interface AttendenceService {
    public boolean userCheckIn(UserCheckinReq userCheckinReq);

    public List<Attendence> getDayCheckIn(DayCheckInReq dayCheckInReq);
}
