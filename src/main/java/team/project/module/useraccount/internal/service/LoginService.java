package team.project.module.useraccount.internal.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.useraccount.internal.mapper.TblUserMapper;
import team.project.module.useraccount.internal.model.entity.TblUserDO;
import team.project.module.useraccount.internal.model.request.RegisterReq;
import team.project.module.useraccount.tmp.service.DepartmentService;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    TblUserMapper tblUserMapper;

    public boolean login(String userId, String password)
    {
        LambdaQueryWrapper<TblUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TblUserDO::getUserId, userId)
            .eq(TblUserDO::getPassword, password);
        List<TblUserDO> userList = tblUserMapper.selectList(lambdaQueryWrapper);

        return userList.size() == 1;
    }
}
