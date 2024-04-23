package team.project.module.club.attendance.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.project.module.club.attendance.internal.service.AttendanceService;

/**
 * 定时任务调度框架
 */
@Component
public class ScheduledTasks {

    @Autowired
    private AttendanceService attendanceService;
    @Scheduled(cron = "59 59 23 * * *") // 在每天23:59:59执行
    public void timedDeleteRecord() {
        // 调用服务类的 TimedDeleteRecord() 方法
        int affectedRows = attendanceService.timedDeleteRecord();
        System.out.println("成功更新记录数：" + affectedRows);
    }
}
