package team.project.module.user.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.user.internal.dao.UserDAO;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.query.SearchUserInfoQO;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    /* -- 查单个 -- */

    /**
     * 查询指定用户的账号信息
     * */
    default UserDO selectUserInfo(String userId) {
        return selectOne(new LambdaQueryWrapper<UserDO>()
            .select(
                UserDO::getUserId,
                UserDO::getDepartmentId,
             /* TblUserDO::getPassword, <- 不查询密码*/
                UserDO::getName,
                UserDO::getTel,
                UserDO::getEmail,
                UserDO::getRole
            )
            .eq(UserDO::getUserId, userId)
        );
    }

    /**
     * 查询指定用户的账号信息
     * */
    default UserDO selectUserInfo(String userId, String password) {
        return selectOne(new LambdaQueryWrapper<UserDO>()
            .eq(UserDO::getUserId, userId)
            .eq(UserDO::getPassword, password)
        );
    }

     /**
     * 查询指定用户的基本信息（只查询姓名和角色，其他属性为 null）
     * */
    default UserDO selectUserBasicInfo(String userId) {
        return selectOne(new LambdaQueryWrapper<UserDO>()
            .select(
                UserDO::getUserId,
                UserDO::getName,
                UserDO::getRole
            )
            .eq(UserDO::getUserId, userId)
        );
    }

    /* -- 查多个 -- */

    /**
     * 详见 {@link UserDAO#searchUserInfo}
     * */
    default List<UserDO> searchUserInfo(Page<UserDO> page, SearchUserInfoQO searchQO) {

        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            UserDO::getUserId,
            UserDO::getDepartmentId,
            UserDO::getName,
            UserDO::getTel,
            UserDO::getEmail,
            UserDO::getRole
        );

        if (null != searchQO.getDepartmentId())
            wrapper.eq(UserDO::getDepartmentId, searchQO.getDepartmentId());
        if (null != searchQO.getUserId())
            wrapper.like(UserDO::getUserId, searchQO.getUserId().replace("%", ""));
        if (null != searchQO.getUserName())
            wrapper.like(UserDO::getName, searchQO.getUserName().replace("%", ""));

        return selectList(page, wrapper);
    }

    /**
     * 详见 {@link UserDAO#searchUserInfo}
     * */
    default List<UserDO> searchUserBasicInfo(String userName) {
        String userNameLike = (userName != null) ? userName.replace("%", "") : "";
        return selectList(new LambdaQueryWrapper<UserDO>()
            .select(
                UserDO::getUserId,
                UserDO::getName,
                UserDO::getRole
            )
            .like(userName != null, UserDO::getName, userNameLike)
        );
    }

    /* -- 增删改 -- */

    /**
     *  将用户账号逻辑删除（注销账户）
     *  @return 如果用户不存在（已注销）或密码错误，则注销失败，返回 0；否则注销成功，返回 1
     * */
    default int logicalDelete(String userId, String password) {
        return update(new LambdaUpdateWrapper<UserDO>()
            .eq(UserDO::getUserId, userId)
            .eq(UserDO::getPassword, password)
            .set(UserDO::getDeleted, true)
        );
    }

    /**
     * 给指定用户设置新角色
     * */
    default int setRole(String userId, int newRole) {
        return update(new LambdaUpdateWrapper<UserDO>()
            .eq(UserDO::getUserId, userId)
            .set(UserDO::getRole, newRole)
        );
    }
}
