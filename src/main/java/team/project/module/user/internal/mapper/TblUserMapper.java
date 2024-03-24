package team.project.module.user.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.user.internal.model.entity.TblUserDO;

import java.util.List;

@Mapper
public interface TblUserMapper extends BaseMapper<TblUserDO> {

    /**
     * 通过学号/工号和密码查询用户（密码经过 service 层加密）
     * @return 如果用户不存在（已注销）或密码错误则返回 false；否则返回 true
     * */
    default boolean isExist(String userId, String password) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        return this.exists(lambdaQueryWrapper
            .eq(TblUserDO::getUserId, userId)
            .eq(TblUserDO::getPassword, password)
        );
    }

    /**
     *  通过学号/工号将用户账号逻辑删除（注销账户）
     *  @return 如果用户不存在（已注销）或密码错误，则注销失败，返回 0；否则注销成功，返回 1
     * */
    default int logicalDelete(String userId, String password) {
        LambdaUpdateWrapper<TblUserDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        return this.update(null, lambdaUpdateWrapper
            .eq(TblUserDO::getUserId, userId)
            .eq(TblUserDO::getPassword, password)
            .set(TblUserDO::getDeleted, true)
        );
    }

    /**
     * 通过学号/工号查询用户信息
     * */
    default TblUserDO selectOne(String userId) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TblUserDO> userList = this.selectList(
            lambdaQueryWrapper.eq(TblUserDO::getUserId, userId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }
}
