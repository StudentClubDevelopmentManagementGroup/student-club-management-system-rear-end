package team.project.module.manage_lin.internal.service;
import org.springframework.beans.factory.annotation.Autowired;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;
import team.project.module.manage_lin.internal.mapper.tbl_club_Mapper;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p style="color: #f23215;">
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

    public int create_club(Long departmentId, String name) {

        if(findbynameBetweendepartmentId(departmentId,name).isEmpty()){
            return tmplMapper.create_club(departmentId, name);
        }else{
            throw new ServiceException(ServiceStatus.CONFLICT, "已存在同院同名社团，请重新输入名字");
        }
    }

    @Override
    public List<tbl_club_DO> findbynameBetweendepartmentId(Long departmentId, String name) {
        if(tmplMapper.findbynamebetweendepartmentId(departmentId, name).isEmpty()){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }
        else {
            return tmplMapper.findbynamebetweendepartmentId(departmentId, name);
        }
    }



    public int delete_club(Long departmentId, String name) {
        if(tmplMapper.delete_club(departmentId, name)==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }else{
            return tmplMapper.delete_club(departmentId, name);
        }

    }

    public int reuse_club(Long departmentId, String name) {
        if(tmplMapper.reuse_club(departmentId, name)==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }
        else {
            return tmplMapper.reuse_club(departmentId, name);
        }
    }
}
