package team.project.module.club.personnelchanges.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.UserMsgDTO;
import team.project.module.club.personnelchanges.internal.model.datatransfer.ClubMemberInfoDTO;

import java.util.List;

@Mapper
public interface TblUserClubMapper extends BaseMapper<TblUserClubDO> {

    int setManager(String userId, Long clubId);

    int createManager(String userId, Long clubId);

    int quashManager(String userId, Long clubId);

    TblUserClubDO selectManagerRole(String userId, Long clubId);

    int createMember(String userId, Long clubId);

    int quashMember(String userId, Long clubId);

    default TblUserClubDO selectOne(String userId, Long clubId) {
        List<TblUserClubDO> userList = this.selectList(new LambdaQueryWrapper<TblUserClubDO>()
                .eq(TblUserClubDO::getUserId, userId)
                .eq(TblUserClubDO::getClubId, clubId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }

    default TblUserClubDO selectRootROle(String userId) { /* review by ljh TODO 改名字 Role 字母 O 小写 */
        List<TblUserClubDO> userList = this.selectList(new LambdaQueryWrapper<TblUserClubDO>()
                .eq(TblUserClubDO::getUserId, userId)
                .in(TblUserClubDO::getRole, 2, 3)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }

    default TblUserClubDO selectMemberRole(String userId) {
        List<TblUserClubDO> userList = this.selectList(new LambdaQueryWrapper<TblUserClubDO>()
                .eq(TblUserClubDO::getUserId, userId)
                .eq(TblUserClubDO::getRole, 1)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }

    Page<ClubMemberInfoDTO> selectClubMemberInfo(@Param("page") Page<ClubMemberInfoDTO> page, Long clubId, String name, Long departmentId);

    Page<UserMsgDTO> selectClubMember(@Param("page") Page<UserMsgDTO> page, Long clubId);
}
