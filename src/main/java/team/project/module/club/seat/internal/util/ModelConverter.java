package team.project.module.club.seat.internal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.seat.internal.model.entity.TblUserClubSeatDO;
import team.project.module.club.seat.internal.model.view.SeatVO;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoIService;

@Component("club-seat-util-ModelConverter")
public class ModelConverter {

    @Autowired
    UserInfoIService userInfoService;

    public SeatVO toSeatVO(TblUserClubSeatDO seat) {

        UserBasicInfoDTO arrangerInfo = userInfoService.selectUserBasicInfo(seat.getArrangerId());
        SeatVO.UserInfoVO arranger = new SeatVO.UserInfoVO();
        arranger.setUserId(seat.getArrangerId());
        arranger.setName(arrangerInfo.getName());
        arranger.setStudent(arrangerInfo.hasRole(UserRole.STUDENT));
        arranger.setTeacher(arrangerInfo.hasRole(UserRole.TEACHER));

        SeatVO.UserInfoVO owner = null;
        if (seat.getOwnerId() != null) {
            UserBasicInfoDTO ownerInfo = userInfoService.selectUserBasicInfo(seat.getOwnerId());
            owner = new SeatVO.UserInfoVO();
            owner.setUserId(seat.getOwnerId());
            owner.setName(ownerInfo.getName());
            owner.setStudent(ownerInfo.hasRole(UserRole.STUDENT));
            owner.setTeacher(ownerInfo.hasRole(UserRole.TEACHER));
        }

        SeatVO seatVO = new SeatVO();
        seatVO.setSeatId(seat.getId());
        seatVO.setSeat(seat.getSeat());
        seatVO.setArranger(arranger);
        seatVO.setOwner(owner);

        return seatVO;
    }
}