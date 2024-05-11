package team.project.module.club.duty.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import team.project.base.model.view.PageVO;
import team.project.module.club.duty.internal.model.entity.TblDuty;
import team.project.module.club.duty.internal.model.query.DutyInfoQO;

import java.sql.Timestamp;
import java.util.List;


public interface DutyService extends IService<TblDuty> {
    void createDuty(String number, String area, Timestamp dutyTime, String arrangerId
            , String cleanerId, Long club_id, Boolean isMixed);

    void createDutyByGroup(String number, String area, Timestamp dutyTime, String arrangerId
            , String cleanerId, Long clubId, Boolean isMixed, String groupName);

    void deleteDutyAllByGroup(Timestamp dutyTime, String groupName, Long club_id);

    void deleteDutyByUser(Timestamp dutyTime, String arrangerId, Long club_id);

    void uploadDutyPicture(Timestamp dutyTime, String memberId, Long clubId, List<MultipartFile> file);

    PageVO<TblDuty> selectDuty(DutyInfoQO qo);

    PageVO<TblDuty> selectDutyByNumber(DutyInfoQO qo);

    PageVO<TblDuty> selectDutyByName(DutyInfoQO qo);
}
