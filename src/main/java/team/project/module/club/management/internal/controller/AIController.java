package team.project.module.club.management.internal.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.club.announcement.export.model.datatransfer.AnnDTO;
import team.project.module.club.announcement.export.service.AnnIServer;
import team.project.module.club.attendance.export.model.datatransfer.AttDTO;
import team.project.module.club.attendance.export.service.AttendanceIService;
import team.project.module.club.management.internal.model.view.AiVO;
import team.project.module.club.personnelchanges.export.model.datatransfer.UserDTO;
import team.project.module.club.personnelchanges.export.model.datatransfer.UserYearlyDTO;
import team.project.module.club.personnelchanges.export.service.PceIService;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@Tag(name = "AI问答")
@RequestMapping("/ai")
public class AIController {

    private final ChatClient chatClient;

    @Autowired
    public AIController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Autowired
    public PceIService pceIService;

    @Autowired
    public AttendanceIService attendanceIService;

    @Autowired
    public AnnIServer annIServer;

    @SaCheckRole(AuthRole.CLUB_MEMBER)
    @Operation(summary = "ai分析")
    @PostMapping("/chat")
    Object generateText(@NotNull(message = "基地ID不能为空") @RequestParam("club_id") Long clubId) {
        AiVO aiVO = new AiVO();
        // 获取数据
        UserYearlyDTO personnelChange = pceIService.getChangeInMembers(clubId);
        List<AttDTO> attitude = attendanceIService.getAttendance(clubId);
        List<UserDTO> userDTOList = pceIService.getChangeInMembersByWeek(clubId);
        AnnDTO annDTO = annIServer.getNum(clubId);
        // 构建结构化提示词
        StringBuilder prompt = new StringBuilder("请根据以下基地数据进行简要快速分析：\n\n")
                .append("年度人数对比\n")
                .append(String.format("今年总人数：%d人 | 去年总人数：%d人\n\n",
                        personnelChange.getTNum(), personnelChange.getLNum()))

                // 新增活动数量模块 ▼▼▼
                .append("活动举办频率对比\n")
                .append(String.format("上个月活动数量：%d次 | 本月活动数量：%d次\n\n",
                        annDTO.getLastMonthNum(), annDTO.getThisMonthNum()))
                // 新增活动数量模块 ▲▲▲

                .append("每周签到对比（按周一到周日排列）\n");

        // 添加签到数据（按周一到周日排序）
        Map<Integer, AttDTO> attMap = new TreeMap<>();
        attitude.forEach(dto -> attMap.put(dto.getDay_of_week(), dto));
        attMap.forEach((day, dto) ->
                prompt.append(String.format("周%s（%d）: 上周签到 %d人 → 本周签到 %d人\n",
                        getChineseWeekday(day), day, dto.getLNum(), dto.getTNum())));

        prompt.append("\n### 每周总人数对比（按周一到周日排列）\n");

        // 添加人数变化数据（按周一到周日排序）
        Map<Integer, UserDTO> userMap = new TreeMap<>();
        userDTOList.forEach(dto -> userMap.put(dto.getDay_of_week(), dto));
        userMap.forEach((day, dto) ->
                prompt.append(String.format("周%s（%d）: 上周总数 %d人 → 本周总数 %d人\n",
                        getChineseWeekday(day), day, dto.getLNum(), dto.getTNum())));

        prompt.append("\n请基于以上数据，简要快速分析：\n"
                + "1. 周同比人数变化趋势\n"
                + "2. 签到率变化情况\n"
                + "3. 活动数量变化情况\n"
                + "4. 给出改进建议");
//        aiVO.setMessage(chatClient.call(prompt.toString()));
        aiVO.setMessage("暂未开通AI功能");
        aiVO.setAnnDTO(annDTO);
        aiVO.setAttDTOList(attitude);
        aiVO.setUserDTOList(userDTOList);
        aiVO.setUserYearlyDTO(personnelChange);

        return new Response<>(ServiceStatus.SUCCESS).statusText("查询成功").data(aiVO);
    }

    // 数字转中文星期
    private String getChineseWeekday(int day) {
        return switch (day) {
            case 1 -> "一";
            case 2 -> "二";
            case 3 -> "三";
            case 4 -> "四";
            case 5 -> "五";
            case 6 -> "六";
            case 7 -> "日";
            default -> "未知";
        };
    }


}





