package team.project.module.club.announcement.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.announcement.internal.model.entity.DraftDO;

@Mapper
public interface DraftMapper extends BaseMapper<DraftDO> {

    default DraftDO selectOneDraftById(Long draftId) {
        return selectOne(new LambdaQueryWrapper<DraftDO>()
            .eq(DraftDO::getDraftId, draftId)
        );
    }
}
