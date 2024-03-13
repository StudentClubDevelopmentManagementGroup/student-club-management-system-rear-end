package team.project.module._template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module._template.model.entity.TmplDO;

import java.util.List;

/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface TmplMapper extends BaseMapper<TmplDO> {

    List<TmplDO> list(); /* 示例 */

    /* 这里定义一些“只有本模块会使用”的方法，
       等确定某个方法会被多个模块使用后，再把它提升到 global.mapper 包中 */
}
