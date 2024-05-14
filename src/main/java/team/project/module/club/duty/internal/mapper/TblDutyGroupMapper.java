package team.project.module.club.duty.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.duty.internal.model.entity.TblDutyGroup;

import java.util.List;

@Mapper
public interface TblDutyGroupMapper extends BaseMapper<TblDutyGroup> {

    int createDutyGroup(Long clubId, String memberId, String name);

    int deleteDutyGroup(Long clubId, String memberId, String name);

    default TblDutyGroup selectByClubIdAndMemberIdAndName(Long clubId, String memberId, String name) {
        List<TblDutyGroup> userList = this.selectList(new LambdaQueryWrapper<TblDutyGroup>().eq(TblDutyGroup::getMember_id, memberId).eq(TblDutyGroup::getClub_id, clubId).eq(TblDutyGroup::getName, name));
        return userList.size() == 1 ? userList.get(0) : null;
    }

    default List<TblDutyGroup> selectUserIdByGroupName(Long clubId, String name) {
        List<TblDutyGroup> userList = this.selectList(new LambdaQueryWrapper<TblDutyGroup>().eq(TblDutyGroup::getClub_id, clubId).eq(TblDutyGroup::getName, name));
        return userList.size() == 1 ? userList : null;
    }


    Page<TblDutyGroup> selectGroup(Page<TblDutyGroup> page, Long clubId);

    Page<TblDutyGroup> selectGroupByName(Page<TblDutyGroup> page, Long clubId, String member_id);

    Page<TblDutyGroup> selectGroupByGroupName(Page<TblDutyGroup> page, Long clubId, String GroupName);

    Page<TblDutyGroup> selectGroupByNameAndGroupName(Page<TblDutyGroup> page, Long clubId, String member_id, String GroupName);
}
