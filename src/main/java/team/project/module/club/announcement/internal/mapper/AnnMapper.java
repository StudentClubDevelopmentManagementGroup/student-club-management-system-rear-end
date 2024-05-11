package team.project.module.club.announcement.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.announcement.internal.model.entity.AnnDO;
import team.project.module.club.announcement.internal.model.query.AnnSearchQO;

import java.util.List;

@Mapper
public interface AnnMapper extends BaseMapper<AnnDO> {

    /**
     *查询公告作者、所属社团，和文本文件的 fileId
     * */
    default AnnDO selectAnnBasicInfo(Long announcementId) {
        return selectOne(new LambdaQueryWrapper<AnnDO>()
            .select(
             /* AnnDO::getDraftId, <- 不查询 id，业务用不到 */
                AnnDO::getAuthorId,
                AnnDO::getClubId,
                AnnDO::getTextFile
            )
            .eq(AnnDO::getAnnouncementId, announcementId)
        );
    }

    /**
     *  搜索公告（分页查询、模糊查询，QO 中不为 null 的字段添入查询条件）
     * */
    default List<AnnDO> searchAnn(Page<AnnDO> page, AnnSearchQO searchQO) {
        LambdaQueryWrapper<AnnDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            AnnDO::getAnnouncementId,
            AnnDO::getPublishTime,
            AnnDO::getAuthorId,
            AnnDO::getClubId,
            AnnDO::getTitle,
            AnnDO::getSummary
        );

        if (null != searchQO.getClubId())
            wrapper.eq(AnnDO::getClubId, searchQO.getClubId());
        if (null != searchQO.getAuthorId())
            wrapper.eq(AnnDO::getAuthorId, searchQO.getAuthorId());
        if (null != searchQO.getTitleKeyword())
            wrapper.like(AnnDO::getTitle, searchQO.getTitleKeyword().replace("%", ""));
        if (null != searchQO.getFromDate())
            wrapper.ge(AnnDO::getPublishTime, searchQO.getFromDate());
        if (null != searchQO.getToDate())
            wrapper.le(AnnDO::getPublishTime, searchQO.getToDate());

        wrapper.orderByDesc(true, AnnDO::getPublishTime); /* 按修改时间排序，新发布的在前面 */

        return selectList(page, wrapper);
    }
}
