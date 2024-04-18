package team.project.module.club.personnelchanges.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.base.model.PageVO;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMasDto;
import team.project.module.club.personnelchanges.internal.model.request.ClubReq;


@Service
public interface TblUserClubService extends IService<TblUserClubDO> {

     void setClubManager(String userId, Long clubId);

     void quashClubManager(String userId, Long clubId);

     void createMember(String userId, Long clubId);

     void quashMember(String userId, Long clubId);

     PageVO<UserMasDto> selectClubMember(ClubReq req);

}

