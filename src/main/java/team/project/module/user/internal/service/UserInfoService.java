package team.project.module.user.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.view.PageVO;
import team.project.module.user.internal.mapper.TblUserMapper;
import team.project.module.user.internal.model.entity.TblUserDO;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TblUserMapper userMapper;

    @Autowired
    ModelConverter modelConverter;

    /**
     * 查询用户账号信息
     * @return 如果查询到成功则账号信息，否则返回 null
     * */
    public UserInfoVO selectUserInfo(String userId) {
        TblUserDO userDO = userMapper.selectOne(userId);
        if (userDO == null) {
            return null;
        }
        return modelConverter.toUserInfoVO(userDO);
    }

    /**
     * 分页查询用户账号信息
     * */
    public PageVO<UserInfoVO> pagingQueryUserInfo(long pageNum, long pageSize) {
        Page<TblUserDO> page = new Page<>(pageNum, pageSize, true);
        List<TblUserDO> userDOList = userMapper.selectList(page, null);

        List<UserInfoVO> userInfoList = new ArrayList<>();
        for (TblUserDO userDO : userDOList) {
            userInfoList.add( modelConverter.toUserInfoVO(userDO) );
        }

        return new PageVO<>(userInfoList, page);
    }
}
