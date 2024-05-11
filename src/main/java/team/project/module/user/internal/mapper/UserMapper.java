package team.project.module.user.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.query.SearchUserInfoQO;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

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
    default UserDO selectBasicInfo(String userId) {
        return selectOne(new LambdaQueryWrapper<UserDO>()
            .select(
                UserDO::getUserId,
                UserDO::getName,
                UserDO::getRole
            )
            .eq(UserDO::getUserId, userId)
        );
    }

    /**
     * 查询指定用户的角色码
     * */
    default Integer selectRoleCode(String userId) {
        UserDO user = selectOne(new LambdaQueryWrapper<UserDO>()
            .select(UserDO::getRole)
            .eq(UserDO::getUserId, userId)
        );
        return user == null ? null : user.getRole();
    }

    /**
     * 搜索相关用户的账号信息（分页查询、模糊查询，QO 中不为 null 的字段添入查询条件）
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
     * 搜索相关用户的基本信息（模糊查询）
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
     * 给指定用户增添角色
     * */
    default int addRoleToUser(String userId, UserRole roleToAdd) {
        /* 2024-03-26 ljh

            1）用户角色的变更与查询，都要依靠 UserRole 提供的方法才能完成
            2）角色码不同的二进制位的含义，只由 UserRole 来赋予，与数据库无关
            3）角色码的维护工作只关系到 UserRole，不涉及其他类，不涉及数据库

            在上述设计理念指导下，“给用户增添角色”需要这样执行：

             1. 先执行查询 sql，获取用户的角色码
             2. 通过 UserRole 获取更新后的角色码（这步封装进了 TblUserDO 的 addRole() 中）
             3. 再执行更新 sql（如果查到的用户已经拥有了角色，就不执行更新 sql）

            这个设计理念，是一种集中度很高的强管理方式，一切操作都要经手 java 层的 UserRole，势必会造成性能损失

            追求性能，最好的方案是：不执行查询 sql，只执行更新 sql，而且将位运算直接落实到 sql
            但能样子做也有前提，要求：位的设计已成定局，各个二进制位的含义、位之间的约束都已明确
            达到这个前提要求后，不仅位运算能直接落实到 sql，各个模块间关于角色的查询和修改，都可以直接操纵位而不需封装
            这样子虽然性能上达到最优，但也使得关于角色码相关的操作零碎的散落在各处代码中，难以维护（难以约束数据合法性）
            试想：
             - 如果一些角色需要不止一个而是多个二进制位才能表示
             - 如果不同角色之间有约束、有牵扯，不能随意增删
            此时，角色的增删不再是简单的单个位的与或操作
            如果 mapper 中位运算直接落实到 sql，那便是不安全的（或者说，一旦代码写错，将难以发现错误）
            只有封装成函数，在函数内做足判断，才能保证角色码的数据合法性

            既然要封装成函数来更新角色码，那“更新前的码”作为函数入参，只能查数据库得到，势必会执行一次查询 sql
            唯一能优化的地方便是：如果查到的用户已经拥有了角色，就不执行更新 sql

            考虑到，现阶段给用户规模很少，更新用户的角色也不是频繁操作，故不打算在此处追求性能
            也就是，我不打算“将位运算直接落实到 sql”
        */

        Integer role = selectRoleCode(userId);

        if (role == null || UserRole.hasRole(role, roleToAdd)) {
            return 0;
        }

        int newRole = UserRole.addRole(role, roleToAdd);

        return update(new LambdaUpdateWrapper<UserDO>()
            .eq(UserDO::getUserId, userId)
            .set(UserDO::getRole, newRole)
        );
    }

    /**
     * 给指定用户移除角色
     * */
    default int removeRoleFromUser(String userId, UserRole roleToRemove) {

        Integer role = this.selectRoleCode(userId);

        if (role == null || ! UserRole.hasRole(role, roleToRemove)) {
            return 0;
        }

        int newRole = UserRole.removeRole(role, roleToRemove);

        return this.update(new LambdaUpdateWrapper<UserDO>()
            .eq(UserDO::getUserId, userId)
            .set(UserDO::getRole, newRole)
        );
    }
}
