package team.project.module.club.personnelchanges.export.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.export.service.PceIService;
import team.project.module.club.personnelchanges.internal.mapper.TblUserClubMapper;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.export.model.request.ClubReq;

@Service
public class PceIServiceImpl implements PceIService {

    @Autowired
    TblUserClubMapper tblUserClubMapper;

    /** review by ljh 2024-04-29 TODO
     *   改代码逻辑，与下方 selectTheMember 同理
     * */
    public boolean isClubManager(String userId, Long clubId) {
        return tblUserClubMapper.selectManagerRole(userId, clubId) != null; // TODO
    }

    public PageVO<UserMsgDTO>  selectClubMember(ClubReq req) {
        Page<UserMsgDTO> user;
        user = tblUserClubMapper.selectClubMember
                (new Page<>(req.getPagenum(), req.getSize()),req.getClubId()
        );
        return new PageVO<>(user);
    }

    /** review by ljh 2024-04-29
     *   判断该用户是否是该社团的成员，可取名 isClubMember 而非 selectTheMember
     *   不应该 Boolean，因为 Boolean 是三值逻辑（true、false、null），而应该返回 boolean
     * TODO: 改名
     * TODO: 改代码逻辑（如果只需判断是否是社团成员，数据库的查询只需返回 boolean。这里不应调用 selectOne，因为这会查询多个字段并装填对象，性能损耗大）
     * */
    public Boolean selectTheMember(String userId, Long clubId) {
        TblUserClubDO user =tblUserClubMapper.selectOne(userId, clubId);
        return user != null;
    }
}
