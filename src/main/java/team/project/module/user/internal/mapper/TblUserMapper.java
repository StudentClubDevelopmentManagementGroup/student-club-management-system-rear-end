package team.project.module.user.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.user.internal.model.entity.TblUserDO;

@Mapper
public interface TblUserMapper extends BaseMapper<TblUserDO> {
}
