package team.project.module.club.announcement.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.club.announcement.internal.model.entity.AnnouncementDO;

@Mapper
public interface AnnouncementMapper extends BaseMapper<AnnouncementDO> {
}
