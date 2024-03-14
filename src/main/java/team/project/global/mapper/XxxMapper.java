package team.project.global.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface XxxMapper// extends BaseMapper<?>
{ /* 用到时，请重命名这个类和对应的 xml */

    /* 这里定义一些“所有模块会可能会使用”的方法，
       等确定某个方法会被多个模块使用后，再把它提升到 global.mapper 包中 */
}
