package team.project.module.user.internal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.base.model.request.PagingQueryReq;
import team.project.base.model.view.PageVO;
import team.project.module.user.internal.dao.UserDAO;
import team.project.module.user.internal.model.entity.UserDO;
import team.project.module.user.internal.model.query.SearchUserInfoQO;
import team.project.module.user.internal.model.request.SearchUserReq;
import team.project.module.user.internal.model.view.UserInfoVO;
import team.project.module.user.internal.util.ModelConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ModelConverter modelConverter;

    /**
     * 查询指定用户的账号信息
     * @return 如果查询到成功则账号信息，否则返回 null
     * */
    public UserInfoVO selectUserInfo(String userId) {
        UserDO userDO = userDAO.selectUserInfo(userId);
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
        List<UserDO> userDOList = userDAO.selectList(page);

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

        String userId       = searchReq.getUserId();
        String userName     = searchReq.getUserName();
        Long   departmentId = searchReq.getDepartmentId();

        SearchUserInfoQO queryQO = new SearchUserInfoQO();
        queryQO.setUserId(  userId   == null || userId.isBlank()   ? null : userId);
        queryQO.setUserName(userName == null || userName.isBlank() ? null : userName );
        queryQO.setDepartmentId(departmentId == null || departmentId.equals(0L) ? null : departmentId);

        List<UserDO> userDOList = userDAO.searchUserInfo(page, queryQO);

        List<UserInfoVO> userInfoList = new ArrayList<>();
        for (UserDO userDO : userDOList) {
            userInfoList.add( modelConverter.toUserInfoVO(userDO) );
        }

        return new PageVO<>(userInfoList, page);
    }
}
