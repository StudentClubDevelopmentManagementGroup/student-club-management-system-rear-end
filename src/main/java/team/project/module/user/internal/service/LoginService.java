package team.project.module.user.internal.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;

import java.util.List;

@Service
public class LoginService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TblUserMapper tblUserMapper;

    public boolean login(String userId, String password)
    {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TblUserDO::getUserId, userId)
            .eq(TblUserDO::getPassword, password);
        List<TblUserDO> userList = tblUserMapper.selectList(lambdaQueryWrapper);

        assert userList.size() <= 1 : "存在同名的账号：" + userId; /* <- 数据库应对 user_id 添加唯一键约束 */

        return userList.size() == 1;
    }
}
