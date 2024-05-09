package team.project.module.user.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.module.user.internal.mapper.UserMapper;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.query.SearchUserInfoQO;
import team.project.module.user.internal.model.request.SearchUserReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserInfoService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ModelConverter modelConverter;

    /**
     * 查询指定用户的账号信息
     * @return 如果查询到成功则账号信息，否则返回 null
     * */
    public UserInfoVO selectUserInfo(String userId) {
        UserDO userDO = userMapper.selectUserInfo(userId);
        if (userDO == null) {
            return null;
        }
        return modelConverter.toUserInfoVO(userDO);
    }

    /**
     * 查询所有用户的账号信息（分页查询）
     * */
    public PageVO<UserInfoVO> selectUserInfo(PagingQueryReq pageReq) {
        Page<UserDO> page = new Page<>(pageReq.getPageNum(), pageReq.getPageSize(), true);
        List<UserDO> userDOList = userMapper.selectList(page, null);

        List<UserInfoVO> userInfoList = new ArrayList<>();
        for (UserDO userDO : userDOList) {
            userInfoList.add( modelConverter.toUserInfoVO(userDO) );
        }

        return new PageVO<>(userInfoList, page);
    }

    /**
    * 搜索用户，返回账号信息（分页查询、模糊查询）
    * */
    public PageVO<UserInfoVO> searchUserInfo(PagingQueryReq pageReq, SearchUserReq searchReq) {

        Page<UserDO> page = new Page<>(pageReq.getPageNum(), pageReq.getPageSize(), true);

        String searchUserId       = searchReq.getUserId();
        String searchUserName     = searchReq.getUserName();
        Long   searchDepartmentId = searchReq.getDepartmentId();

        SearchUserInfoQO queryQO = new SearchUserInfoQO();
        queryQO.setUserId(   "".equals(searchUserId)   ? null : searchUserId   );
        queryQO.setUserName( "".equals(searchUserName) ? null : searchUserName );
        queryQO.setDepartmentId(Objects.equals(0L, searchDepartmentId) ? null : searchDepartmentId);

        List<UserDO> userDOList = userMapper.searchUserInfo(page, queryQO);

        List<UserInfoVO> userInfoList = new ArrayList<>();
        for (UserDO userDO : userDOList) {
            userInfoList.add( modelConverter.toUserInfoVO(userDO) );
        }

        return new PageVO<>(userInfoList, page);
    }
}
