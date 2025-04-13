package team.project.module.club.attendance.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.club.attendance.export.model.datatransfer.AttDTO;
import team.project.module.club.attendance.export.service.AttendanceIService;
import team.project.module.club.attendance.internal.mapper.AttendanceMapper;

import java.util.List;
@Service
public class AttendanceIServiceImpl implements AttendanceIService {
    @Autowired
     public AttendanceMapper attendanceMapper;
    @Override
    public List<AttDTO> getAttendance(Long clubId) {
        return attendanceMapper.getWeeklyAttendanceRecord(clubId);
    }
}
