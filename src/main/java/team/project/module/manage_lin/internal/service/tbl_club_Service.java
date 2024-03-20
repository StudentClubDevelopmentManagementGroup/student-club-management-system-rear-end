package team.project.module.manage_lin.internal.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;
import java.util.List;

@Service
public  interface tbl_club_Service extends IService<tbl_club_DO> {


     void create_club(Long departmentId, String name);

     List<tbl_club_DO> findbynameBetweendepartmentId(Long departmentId, String name);

     Page<tbl_club_DO> selectPageBynamebetweendepartmentId(@Param("page") Page<tbl_club_DO> page, Long departmentId, String name);

     void delete_club(Long id, String name);

     void reuse_club(Long id, String name);

     void deactivate_clb(Long departmentId, String name);

     void recover_club(Long departmentId, String name);
}
