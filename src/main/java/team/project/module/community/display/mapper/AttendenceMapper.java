package team.project.module.community.display.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.community.display.model.entity.Attendence;

//如果使用Mybatis-plus提供的方法不需要再写.xml映射文件
//复杂的数据库操作需要写SQL
@Mapper
public interface AttendenceMapper extends BaseMapper<Attendence> {

}





