package team.project.module.manage_lin.internal.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.module.manage_lin.internal.model.entity.tbl_club_DO;
import java.util.List;

@Service
public  interface tbl_club_Service extends IService<tbl_club_DO> {


     List<tbl_club_DO> findbyname(String name);

     int create_club(Long departmentId, String name);

     List<tbl_club_DO> findbynameBetweendepartmentId(Long departmentId, String name);

     int delete_club(Long id, String name);

     int reuse_club(Long id, String name);
}
