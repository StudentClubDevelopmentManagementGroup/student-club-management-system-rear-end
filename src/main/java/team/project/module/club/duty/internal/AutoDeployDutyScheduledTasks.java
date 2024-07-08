package team.project.module.club.duty.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.project.module.club.duty.internal.mapper.TblDutyCirculationMapper;
import team.project.module.club.duty.internal.mapper.TblDutyMapper;
import team.project.module.club.duty.internal.model.entity.TblDuty;
import team.project.module.club.duty.internal.model.entity.TblDutyCirculation;
import team.project.module.club.duty.internal.service.DutyService;
import team.project.module.util.email.export.model.query.SendEmailQO;
import team.project.module.util.email.export.service.EmailServiceI;

import java.time.LocalDateTime;
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
    private EmailServiceI emailService;

    @Autowired
    private TblDutyCirculationMapper tblDutyCirculationMapper;

    @Scheduled(cron = "0 0 23 ? * 7")
    public void autoDeployDuty() {
        try {
            List<TblDutyCirculation> tblDutyCirculations = tblDutyCirculationMapper.selectAutoDutyClubId();
            // 使用List<TblDutyCirculation> tblDuties = new ArrayList<>();来收集所有需要创建的职责信息，以减少数据库访问
            List<TblDuty> tblDuties = new ArrayList<>();
            for (TblDutyCirculation tblDutyCirculation : tblDutyCirculations) {
                if (dutyMapper.selectNextWeek(tblDutyCirculation.getClubId()) != null) {
                    continue;
                }
                List<TblDuty> duties = dutyMapper.selectLastWeek(tblDutyCirculation.getClubId());
                tblDuties.addAll(duties);
            }
            // 执行批量创建职责操作
            for (TblDuty duty : tblDuties) {
                LocalDateTime timestamp0 = duty.getDutyTime();
                // 使用java.time进行日期操作
                LocalDateTime nextWeekDateTime = timestamp0.plusWeeks(1);
                dutyService.createDuty(duty.getNumber(), duty.getArea(), nextWeekDateTime, duty.getArrangerId(), duty.getCleanerId(), duty.getClubId(), duty.getIsMixed());
            }
        } catch (Exception e) {
            log.error("自动部署职责任务时发生异常: ", e);
        }
    }

    @Scheduled(cron = "0 0 23 * * ?")
    public void autoDutyMail() {
        try {
            List<TblDuty> TblDuty = dutyMapper.selectDutyTomorrow();
            for (TblDuty tblDuty : TblDuty) {
                SendEmailQO sendEmailQO = new SendEmailQO();
                sendEmailQO.setSendTo(tblDuty.getCleanerId());
                sendEmailQO.setSubject("值日提醒");
                sendEmailQO.setContent("值日内容：" + " " + "值日时间" + tblDuty.getDutyTime() + " " + "值日地点：" + tblDuty.getNumber() + "值日区域" + tblDuty.getArea());
                sendEmailQO.setHtml(true);
                emailService.sendEmail(sendEmailQO);
            }

            List<TblDuty> TblDuty1 = dutyMapper.selectDutyTodayNotFinished();
            for (TblDuty tblDuty : TblDuty1) {
                SendEmailQO sendEmailQO = new SendEmailQO();
                sendEmailQO.setSendTo(tblDuty.getArrangerId());
                sendEmailQO.setSubject("值日提醒");
                sendEmailQO.setContent("值日未完成提醒：" + " " + "值日时间" + tblDuty.getDutyTime() + " " + "值日地点：" + tblDuty.getNumber() + "值日区域" + tblDuty.getArea());
                sendEmailQO.setHtml(true);
                emailService.sendEmail(sendEmailQO);
            }

        } catch (Exception e) {
            log.error("自动发送值日邮件任务时发生异常: ", e);
        }


    }
}
