package team.project.module.user.internal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.enums.UserRoleEnum;

import java.util.List;

@Mapper
public interface TblUserMapper extends BaseMapper<TblUserDO> {

    /**
     * 查询用户信息（所有字段都查询）
     * */
    default TblUserDO selectOne(String userId) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TblUserDO> userList = this.selectList(lambdaQueryWrapper
            .eq(TblUserDO::getUserId, userId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }

    /**
     * 查询用户信息（所有字段都查询）
     * */
    default TblUserDO selectOne(String userId, String password) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TblUserDO> userList = this.selectList(lambdaQueryWrapper
            .eq(TblUserDO::getUserId, userId)
            .eq(TblUserDO::getPassword, password)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }

     /**
     * 查询用户基本信息（只查询姓名和角色，其他属性为 null）
     * */
    default TblUserDO selectBasicInfo(String userId) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TblUserDO> userList = this.selectList(lambdaQueryWrapper
            .select(TblUserDO::getName)
            .select(TblUserDO::getRole)
            .eq(TblUserDO::getUserId, userId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }

    /**
     * 查询用户角色（只查询角色，其他属性为 null）
     * */
    default TblUserDO selectRole(String userId) {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TblUserDO> userList = this.selectList(lambdaQueryWrapper
            .select(TblUserDO::getRole)
            .eq(TblUserDO::getUserId, userId)
        );
        return userList.size() == 1 ? userList.get(0) : null;
    }

    /**
     *  将用户账号逻辑删除（注销账户）
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
     * 给用户增添角色
     * */
    default int addRoleToUser(String userId, UserRoleEnum roleToAdd) {
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

        TblUserDO user = this.selectRole(userId);
        if (user == null) {
            return 0;
        }

        if (user.hasRole(roleToAdd)) {
            return 0;
        }
        user.addRole(roleToAdd);

        LambdaUpdateWrapper<TblUserDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        return this.update(null, lambdaUpdateWrapper
            .eq(TblUserDO::getUserId, userId)
            .set(TblUserDO::getRole, user.getRole())
        );
    }

    /**
     * 给用户移除角色
     * */
    default int removeRoleFromUser(String userId, UserRoleEnum roleToRemove) {
        TblUserDO user = this.selectRole(userId);
        if (user == null) {
            return 0;
        }

        if ( ! user.hasRole(roleToRemove)) {
            return 0;
        }
        user.removeRole(roleToRemove);

        LambdaUpdateWrapper<TblUserDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        return this.update(null, lambdaUpdateWrapper
            .eq(TblUserDO::getUserId, userId)
            .set(TblUserDO::getRole, user.getRole())
        );
    }
}
