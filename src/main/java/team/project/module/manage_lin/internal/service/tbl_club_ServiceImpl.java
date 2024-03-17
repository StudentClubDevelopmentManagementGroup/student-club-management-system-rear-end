package team.project.module.manage_lin.internal.service;
import org.springframework.beans.factory.annotation.Autowired;
import team.project.module.manage_lin.internal.mapper.tbl_club_Mapper;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 基地表 服务实现类
 * </p>
 *
 * @author LIn
 * @since 2024-03-17
 */
@Service
public class tbl_club_ServiceImpl extends ServiceImpl<tbl_club_Mapper, tbl_club_DO> implements tbl_club_Service {
    @Autowired
    tbl_club_Mapper tmplMapper;  /* 示例 */

    public List<tbl_club_DO> findbyname(String name) { /* 示例 */
        return tmplMapper.findbyname(name);
    }

    public List<tbl_club_DO> create_club(Long departmentId, String name) {
        return tmplMapper.create_club(departmentId, name);
    }
}
