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
     * 查询单篇公告的基本信息，即只查询公告作者、所属社团
     * */
    default AnnDO selectAnnBasicInfo(Long announcementId) {
        return selectOne(new LambdaQueryWrapper<AnnDO>()
            .select(
                AnnDO::getAuthorId,
                AnnDO::getClubId
            )
            .eq(AnnDO::getAnnouncementId, announcementId)
        );
    }

    /**
     * 搜索公告（分页查询、模糊查询，QO 中不为 null 的字段添入查询条件）
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

        if ( ! searchQO.getClubIdColl().isEmpty())
            wrapper.in(AnnDO::getClubId, searchQO.getClubIdColl());
        if ( ! searchQO.getAuthorIdColl().isEmpty())
            wrapper.in(AnnDO::getAuthorId, searchQO.getAuthorIdColl());
        if (null != searchQO.getTitleKeyword())
            wrapper.like(AnnDO::getTitle, searchQO.getTitleKeyword().replace("%", ""));
        if (null != searchQO.getFromDate())
            wrapper.ge(AnnDO::getPublishTime, searchQO.getFromDate());
        if (null != searchQO.getToDate())
            wrapper.le(AnnDO::getPublishTime, searchQO.getToDate().plusDays(1)); /* <- 多增一天，以包含 to_date 当天 */

        wrapper.orderByDesc(true, AnnDO::getPublishTime); /* 按修改时间排序，新发布的在前面 */

        return selectList(page, wrapper);
    }
}
