package team.project.module.club.announcement.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.announcement.internal.model.entity.DraftDO;

import java.util.List;

@Mapper
public interface DraftMapper extends BaseMapper<DraftDO> {

    default List<DraftDO> listMyDraft(Page<DraftDO> page, String authorId, Long clubId) {
        return selectList(page, new LambdaQueryWrapper<DraftDO>()
            .eq(DraftDO::getClubId, clubId)
            .eq(DraftDO::getAuthorId, authorId)
            .orderByDesc(true, DraftDO::getUpdateTime)
        );
    }
}
