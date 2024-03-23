package team.project.module.user.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.user.internal.model.entity.TblUserDO;

import java.util.List;

@Mapper
public interface TblUserMapper extends BaseMapper<TblUserDO> {
    default TblUserDO selectByUserId(String userId) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TblUserDO::getUserId, userId);
        List<TblUserDO> userList = this.selectList(lambdaQueryWrapper);

        assert userList.size() <= 1 : "存在同名的账号：" + userId; /* <- 数据库应对 user_id 添加唯一键约束 */

        return userList.size() == 1 ? userList.get(0) : null;
    }
}
