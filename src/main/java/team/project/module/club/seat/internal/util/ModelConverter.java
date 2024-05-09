package team.project.module.club.seat.internal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.club.seat.internal.model.entity.SeatDO;
import team.project.module.club.seat.internal.model.view.ClubMemberInfoVO;
import team.project.module.club.seat.internal.model.view.SeatVO;
import team.project.module.user.export.model.datatransfer.UserBasicInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;

@Component("club-seat-util-ModelConverter")
public class ModelConverter {

    @Autowired
    UserInfoServiceI userInfoService;

    public SeatVO toSeatVO(SeatDO seat) {

        UserBasicInfoDTO arrangerDTO = userInfoService.selectUserBasicInfo(seat.getArrangerId());
        ClubMemberInfoVO arrangerVO = toClubMemberInfoVO(arrangerDTO);

        ClubMemberInfoVO ownerVO = null;
        if (seat.getOwnerId() != null) {
            UserBasicInfoDTO ownerDTO = userInfoService.selectUserBasicInfo(seat.getOwnerId());
            ownerVO = toClubMemberInfoVO(ownerDTO);
        }

        SeatVO seatVO = new SeatVO();
        seatVO.setSeatId(seat.getSeatId());
        seatVO.setX(seat.getX());
        seatVO.setY(seat.getY());
        seatVO.setDescription(seat.getDescription());
        seatVO.setArranger(arrangerVO);
        seatVO.setOwner(ownerVO);

        return seatVO;
    }

    public ClubMemberInfoVO toClubMemberInfoVO(UserBasicInfoDTO userInfo) {

        ClubMemberInfoVO result = new ClubMemberInfoVO();
        result.setUserId(userInfo.getUserId());
        result.setName(userInfo.getName());
        result.setStudent(userInfo.hasRole(UserRole.STUDENT));
        result.setTeacher(userInfo.hasRole(UserRole.TEACHER));

        return result;
    }
}
