package team.project.module.club.attendance.export.service;

import org.springframework.stereotype.Service;
import team.project.module.club.attendance.export.model.datatransfer.AttDTO;

import java.util.List;

public interface AttendanceIService {
    public List<AttDTO> getAttendance(Long clubId);
}
