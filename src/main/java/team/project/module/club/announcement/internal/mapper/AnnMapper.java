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
        if (null != searchQO.getTitleKeyword())
            wrapper.like(AnnDO::getTitle, searchQO.getTitleKeyword().replace("%", ""));

        wrapper.orderByDesc(true, AnnDO::getPublishTime); /* 按修改时间排序，新发布的在前面 */

        return selectList(page, wrapper);
    }
}
