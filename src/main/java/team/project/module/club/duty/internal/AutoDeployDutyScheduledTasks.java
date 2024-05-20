package team.project.module.club.duty.internal;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.project.module.club.duty.internal.mapper.TblDutyCirculationMapper;
import team.project.module.club.duty.internal.mapper.TblDutyMapper;
import team.project.module.club.duty.internal.model.entity.TblDuty;
import team.project.module.club.duty.internal.model.entity.TblDutyCirculation;
import team.project.module.club.duty.internal.service.DutyService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AutoDeployDutyScheduledTasks {

    @Autowired
    private DutyService dutyService;

    @Autowired
    private TblDutyMapper dutyMapper;

    @Autowired
    private TblDutyCirculationMapper tblDutyCirculationMapper;

    @Scheduled(cron = "0 0 23 ? * 7")
    public void autoDeployDuty() {
        try {
            List<TblDutyCirculation> tblDutyCirculations = tblDutyCirculationMapper.selectAutoDutyClubid();
            // 使用List<TblDuty> tblDuties = new ArrayList<>();来收集所有需要创建的职责信息，以减少数据库访问
            List<TblDuty> tblDuties = new ArrayList<>();
            for (TblDutyCirculation tblDutyCirculation : tblDutyCirculations) {
                if(dutyMapper.selectNextWeek(tblDutyCirculation.getClub_id())!=null){
                    continue;
                }
                List<TblDuty> duties = dutyMapper.selectLastWeek(tblDutyCirculation.getClub_id());
                tblDuties.addAll(duties);
            }
            // 执行批量创建职责操作
            for (TblDuty duty : tblDuties) {
                Timestamp timestamp0 = duty.getDuty_time();
                // 使用java.time进行日期操作
                LocalDateTime nextWeekDateTime = timestamp0.toLocalDateTime().plusWeeks(1);
                Timestamp nextWeekTimestamp = Timestamp.from(nextWeekDateTime.atZone(ZoneId.systemDefault()).toInstant());
                dutyService.createDuty(duty.getNumber(), duty.getArea(), nextWeekTimestamp, duty.getArranger_id(), duty.getCleaner_id(), duty.getClub_id(), duty.getIs_mixed());
            }
        } catch (Exception e) {
            log.error("自动部署职责任务时发生异常: " + e.getMessage());
        }
    }
}
