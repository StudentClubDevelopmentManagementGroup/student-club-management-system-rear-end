package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.model.view.PageVO;
import team.project.module.club.duty.internal.model.entity.TblDuty;
import team.project.module.club.duty.internal.model.query.DutyInfoQO;
import team.project.module.club.duty.internal.model.query.DutyInfoSelfQO;
import team.project.module.club.duty.internal.model.view.DutyInfoVO;

import java.time.LocalDateTime;
import java.util.List;


public interface DutyService extends IService<TblDuty> {
    void createDuty(String number, String area, LocalDateTime dutyTime, String arrangerId
            , String cleanerId, Long clubId, Boolean isMixed);

    void createDutyByGroup(String number, String area, LocalDateTime dutyTime, String arrangerId
            , Long clubId, Boolean isMixed, String groupName);

    void deleteDutyAllByGroup(LocalDateTime dutyTime, String groupName, Long clubId);

    void deleteDutyByUser(LocalDateTime dutyTime, String arrangerId, Long clubId);

    List<String> uploadDutyPictures(LocalDateTime dutyTime, String memberId, Long clubId, MultipartFile[] file);

    PageVO<DutyInfoVO> selectDuty(DutyInfoQO qo);

    PageVO<DutyInfoVO> selectDutyByNumber(DutyInfoQO qo);

    PageVO<DutyInfoVO> selectDutyByName(DutyInfoQO qo);

    PageVO<DutyInfoVO> selectDutyByNumberAndName(DutyInfoQO qo);

    PageVO<DutyInfoVO> selectDutyByUserId(DutyInfoSelfQO qo, String userId);
}
