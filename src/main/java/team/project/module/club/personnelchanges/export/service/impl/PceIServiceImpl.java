package team.project.module.club.personnelchanges.export.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.PageVO;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.club.personnelchanges.internal.mapper.TblUserClubMapper;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.export.model.request.ClubReq;


@Service
public class PceIServiceImpl implements PceIService {

    @Autowired
    TblUserClubMapper tblUserClubMapper;
    public boolean isClubManager(String userId, Long clubId)
    {
        return tblUserClubMapper.selectManagerRole(userId, clubId) != null;
    }

    public PageVO<UserMsgDTO>  selectClubMember(ClubReq req) {
        Page<UserMsgDTO> user;
        user = tblUserClubMapper.selectClubMember(
                new Page<>(req.getPagenum(), req.getSize()),req.getClubId());
        return new PageVO<>(user);
    }

    public Boolean selectTheMember(String userId, Long clubId) {
        TblUserClubDO user =tblUserClubMapper.selectOne(userId, clubId);
        return user != null;
    }
}
