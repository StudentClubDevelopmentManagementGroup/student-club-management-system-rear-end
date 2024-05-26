package team.project.module.club.announcement.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.announcement.internal.model.entity.DraftDO;

import java.util.List;

@Mapper
public interface DraftMapper extends BaseMapper<DraftDO> {

    /**
     * 查询单篇草稿的基本信息，即只查询草稿作者、所属社团、和对应文本文件的 fileId
     * */
    default DraftDO selectDraftBasicInfo(Long draftId) {
        return selectOne(new LambdaQueryWrapper<DraftDO>()
            .select(
             /* DraftDO::getDraftId, <- 不查询 id，业务用不到 */
                DraftDO::getAuthorId,
                DraftDO::getClubId,
                DraftDO::getTextFile
            )
            .eq(DraftDO::getDraftId, draftId)
        );
    }

    /**
     * 分页查询我的草稿（查询所有字段）
     * */
    default List<DraftDO> selectListMyDraft(Page<DraftDO> page, String authorId, Long clubId) {
        return selectList(page, new LambdaQueryWrapper<DraftDO>()
            .eq(DraftDO::getClubId, clubId)
            .eq(DraftDO::getAuthorId, authorId)
            .orderByDesc(true, DraftDO::getUpdateTime) /* 按修改时间排序，新修改的在前面 */
        );
    }

    /**
     * 查询我的所有草稿，但只查询草稿 id 和对应文本文件的 fileId
     * */
    default List<DraftDO> selectAllMyDraftFile(String userId, Long clubId) {
        return selectList(new LambdaQueryWrapper<DraftDO>()
            .select(
                DraftDO::getDraftId,
                DraftDO::getTextFile
            )
            .eq(DraftDO::getClubId, clubId)
            .eq(DraftDO::getAuthorId, userId)
        );
    }
}
