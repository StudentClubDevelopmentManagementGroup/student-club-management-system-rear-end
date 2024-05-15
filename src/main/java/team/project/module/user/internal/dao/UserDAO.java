package team.project.module.user.internal.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.internal.mapper.UserMapper;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.query.SearchUserInfoQO;

import java.util.List;

@Component
public class UserDAO {

    @Autowired
    private UserMapper userMapper;

    /**
     * 缓存，只缓存用户的基本信息
     * */
    private final LoadingCache<String, UserDO> userBasicInfoCache = Caffeine.newBuilder().build(
        userId -> userMapper.selectUserBasicInfo(userId)
    );

    /* -- 封装自定义 sql -- */

    /* -- 查单个 -- */

    /**
     * 查询指定用户的账号信息
     * */
    public UserDO selectUserInfo(String userId) {

        UserDO result = userMapper.selectUserInfo(userId);

        if (result != null) {
            userBasicInfoCache.put(userId, result);
        }

        return result;
    }

    /**
     * 查询指定用户的账号信息
     * */
    public UserDO selectUserInfo(String userId, String password) {

        UserDO result = userMapper.selectUserInfo(userId, password);

        if (result != null) {
            userBasicInfoCache.put(userId, result);
        }

        return result;
    }

    /**
     * 查询指定用户的基本信息，只需从缓存中拿取
     * */
    public UserDO selectUserBasicInfo(String userId) {
        return userBasicInfoCache.get(userId);
    }

    /**
     * 查询指定用户的角色码
     * */
    public Integer selectRoleCode(String userId) {
        return selectUserBasicInfo(userId).getRole();
    }

    /* -- 查多个 -- */

    /**
     * 搜索相关用户的账号信息（分页查询、模糊查询，QO 中不为 null 的字段添入查询条件）
     * */
    public List<UserDO> searchUserInfo(Page<UserDO> page, SearchUserInfoQO searchQO) {
        return userMapper.searchUserInfo(page, searchQO);
    }

    /**
     * 搜索相关用户的基本信息（模糊查询）
     * */
    public List<UserDO> searchUserBasicInfo(String userName) {
        return userMapper.searchUserBasicInfo(userName);
    }

    /* -- 增删改 -- */

    /**
     *  将用户账号逻辑删除（注销账户）
     *  @return 如果用户不存在（已注销）或密码错误，则注销失败，返回 0；否则注销成功，返回 1
     * */
    public int logicalDelete(String userId, String password) {
        userBasicInfoCache.invalidate(userId);
        return userMapper.logicalDelete(userId, password);
    }

    /**
     * 给指定用户增添角色
     * */
    public int addRoleToUser(String userId, UserRole roleToAdd) {

        /* 2024-03-26 ljh
           ---------

            1）用户角色的变更与查询，都要依靠 UserRole 提供的方法才能完成
            2）角色码不同的二进制位的含义，只由 UserRole 来赋予，与数据库无关
            3）角色码的维护工作只关系到 UserRole，不涉及其他类，不涉及数据库

            在上述设计理念指导下，“给用户增添角色”需要这样执行：

             1. 先执行查询 sql，获取用户的角色码
             2. 通过 UserRole 获取更新后的角色码
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

           2024-05-13 ljh
           ---------
            使用缓存优化查询 sql
            原先此注释位于 mapper 层，先移至 DAO 层
        */

        Integer role = selectRoleCode(userId);

        if (role == null || UserRole.hasRole(role, roleToAdd)) {
            return 0;
        }

        userBasicInfoCache.invalidate(userId);

        return userMapper.setRole(userId, UserRole.addRole(role, roleToAdd));
    }

    /**
     * 给指定用户移除角色
     * */
    public int removeRoleFromUser(String userId, UserRole roleToRemove) {

        Integer role = selectRoleCode(userId);

        if (role == null || ! UserRole.hasRole(role, roleToRemove)) {
            return 0;
        }

        userBasicInfoCache.invalidate(userId);

        return userMapper.setRole(userId, UserRole.removeRole(role, roleToRemove));
    }

    /* -- 封装 mybatis-plus BaseMapper 的 CRUD 方法 -- */

    public int insert(UserDO user) {
        return userMapper.insert(user);
    }

    public List<UserDO> selectList(Page<UserDO> page) {
        return userMapper.selectList(page, null);
    }
}
