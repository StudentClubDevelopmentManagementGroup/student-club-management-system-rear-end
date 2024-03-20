package team.project.module.manage_lin.internal.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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


    public void create_club(Long departmentId, String name) {

        if(findbynameBetweendepartmentId(departmentId,name).isEmpty()){
            tmplMapper.create_club(departmentId, name);
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

    @Override
    public Page<tbl_club_DO> selectPageBynamebetweendepartmentId(Page<tbl_club_DO> page,Long departmentId, String name) {
        if(tmplMapper.selectPageBynamebetweendepartmentId(page,departmentId, name).getTotal()==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }
        else {
            return tmplMapper.selectPageBynamebetweendepartmentId(page,departmentId, name);
        }
    }

    @Override
    public Page<tbl_club_DO> selectPageByname(Page<tbl_club_DO> page, String name) {
        if(tmplMapper.selectPageByname(page, name).getTotal()==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }
        else {
            return tmplMapper.selectPageByname(page, name);
        }
    }

    @Override
    public Page<tbl_club_DO> selectPageBydepartmentId(Page<tbl_club_DO> page, Long departmentId) {
        if(tmplMapper.selectPageBydepartmentId(page, departmentId).getTotal()==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }
        else {
            return tmplMapper.selectPageBydepartmentId(page, departmentId);
        }
    }

    public void delete_club(Long departmentId, String name) {
        if(tmplMapper.delete_club(departmentId, name)==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }else{
            tmplMapper.delete_club(departmentId, name);
        }

    }

    public void reuse_club(Long departmentId, String name) {
        if(tmplMapper.reuse_club(departmentId, name)==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }
        else {
            tmplMapper.reuse_club(departmentId, name);
        }
    }

    @Override
    public void deactivate_clb(Long departmentId, String name) {
        if(tmplMapper.deactivate_clb(departmentId, name)==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }
        else {
            tmplMapper.deactivate_clb(departmentId, name);
        }
    }

    @Override
    public void recover_club(Long departmentId, String name) {
        if(tmplMapper.recover_club(departmentId, name)==0){
            throw new ServiceException(ServiceStatus.NOT_FOUND, "未找到该社团，请重新输入名字");
        }else{
            tmplMapper.recover_club(departmentId, name);
        }
    }
}
