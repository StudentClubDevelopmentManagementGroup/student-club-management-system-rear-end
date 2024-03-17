package team.project.module.manage_lin.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module._template.internal.model.entity.TmplDO;

import java.util.List;

/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface TmplMapper extends BaseMapper<TmplDO> {

    List<TmplDO> list(); /* 示例 */
}
