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
     * */
    default int cancelAccountByPassword(String userId, String password) {
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
    default TblUserDO selectUserInfoByUserId(String userId) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TblUserDO> userList = this.selectList(
            lambdaQueryWrapper.eq(TblUserDO::getUserId, userId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }
}
