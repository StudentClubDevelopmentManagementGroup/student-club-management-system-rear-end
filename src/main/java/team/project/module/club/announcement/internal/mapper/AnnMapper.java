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

        if (null != searchQO.getFromDate())
            wrapper.ge(AnnDO::getPublishTime, searchQO.getFromDate());
        if (null != searchQO.getToDate())
            wrapper.le(AnnDO::getPublishTime, searchQO.getToDate().plusDays(1)); /* <- 多增一天，以包含 to_date 当天 */
        if (null != searchQO.getTitleKeyword())
            wrapper.like(AnnDO::getTitle, searchQO.getTitleKeyword().replace("%", ""));
        if ( ! searchQO.getClubIdColl().isEmpty())
            wrapper.in(AnnDO::getClubId, searchQO.getClubIdColl());
        if ( ! searchQO.getAuthorIdColl().isEmpty())
            wrapper.in(AnnDO::getAuthorId, searchQO.getAuthorIdColl());

        wrapper.orderByDesc(AnnDO::getPublishTime); /* 按发布时间排序，新发布的在前面 */

        return selectList(page, wrapper);
    }

    //社团招新查询
    default List<AnnDO> searchRecruitment(Page<AnnDO> page, AnnSearchQO searchQO) {
        LambdaQueryWrapper<AnnDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                AnnDO::getAnnouncementId,
                AnnDO::getPublishTime,
                AnnDO::getAuthorId,
                AnnDO::getClubId,
                AnnDO::getTitle,
                AnnDO::getSummary
        );

        // 1. 用户输入的关键词查询（保留原有逻辑）
        if (null != searchQO.getTitleKeyword()) {
            String sanitizedKeyword = searchQO.getTitleKeyword().replace("%", ""); // 防止用户输入通配符
            wrapper.like(AnnDO::getTitle, sanitizedKeyword); // 示例：title LIKE '%用户关键词%'
        }

        // 2. 强制要求标题必须包含“招新”（新增逻辑）
        wrapper.like(AnnDO::getTitle, "招新"); // 自动添加通配符：title LIKE '%招新%'

        // 其他原有条件保持不变
        if (null != searchQO.getFromDate())
            wrapper.ge(AnnDO::getPublishTime, searchQO.getFromDate());
        if (null != searchQO.getToDate())
            wrapper.le(AnnDO::getPublishTime, searchQO.getToDate().plusDays(1));
        if (!searchQO.getClubIdColl().isEmpty())
            wrapper.in(AnnDO::getClubId, searchQO.getClubIdColl());
        if (!searchQO.getAuthorIdColl().isEmpty())
            wrapper.in(AnnDO::getAuthorId, searchQO.getAuthorIdColl());

        wrapper.orderByDesc(AnnDO::getPublishTime);

        return selectList(page, wrapper);
    }


    default AnnDO selectLatestOne(Long clubId) {
        return selectOne(new LambdaQueryWrapper<AnnDO>().select(
                AnnDO::getAnnouncementId,
                AnnDO::getPublishTime,
                AnnDO::getAuthorId,
                AnnDO::getClubId,
                AnnDO::getTitle,
                AnnDO::getTextFile
            )
            .eq(AnnDO::getClubId, clubId)
            .orderByDesc(AnnDO::getPublishTime)
            .last("limit 1")
        );
    }

    default List<AnnDO> searchActivity(Page<AnnDO> page, AnnSearchQO searchQO) {
        LambdaQueryWrapper<AnnDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                AnnDO::getAnnouncementId,
                AnnDO::getPublishTime,
                AnnDO::getAuthorId,
                AnnDO::getClubId,
                AnnDO::getTitle,
                AnnDO::getSummary
        );

        // 用户输入的关键词查询
        if (null != searchQO.getTitleKeyword()) {
            String sanitizedKeyword = searchQO.getTitleKeyword().replace("%", "");
            wrapper.like(AnnDO::getTitle, sanitizedKeyword);
        }

        // 强制要求标题包含 "比赛" 或 "活动" 或 "大赛"
        wrapper.and(wq -> wq
                .like(AnnDO::getTitle, "比赛")
                .or()
                .like(AnnDO::getTitle, "活动")
                .or()
                .like(AnnDO::getTitle, "大赛")
                .or()
                .like(AnnDO::getTitle, "竞赛")
        );

        // 其他条件
        if (null != searchQO.getFromDate())
            wrapper.ge(AnnDO::getPublishTime, searchQO.getFromDate());
        if (null != searchQO.getToDate())
            wrapper.le(AnnDO::getPublishTime, searchQO.getToDate().plusDays(1));
        if (!searchQO.getClubIdColl().isEmpty())
            wrapper.in(AnnDO::getClubId, searchQO.getClubIdColl());
        if (!searchQO.getAuthorIdColl().isEmpty())
            wrapper.in(AnnDO::getAuthorId, searchQO.getAuthorIdColl());

        wrapper.orderByDesc(AnnDO::getPublishTime);

        return selectList(page, wrapper);
    }


}
