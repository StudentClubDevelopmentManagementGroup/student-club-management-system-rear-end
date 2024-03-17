package team.project.module.manage_lin.internal.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module._template.internal.mapper.TmplMapper;
import team.project.module.manage_lin.internal.mapper.tbl_club_Mapper;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public  interface tbl_club_Service extends IService<tbl_club_DO> {


    public List<tbl_club_DO> findbyname(String name);

    public List<tbl_club_DO> create_club(Long departmentId, String name);
    

}
