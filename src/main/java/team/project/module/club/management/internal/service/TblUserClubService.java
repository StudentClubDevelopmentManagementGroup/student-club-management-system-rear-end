package team.project.module.club.management.internal.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;


@Service
public interface TblUserClubService extends IService<TblUserClubDO> {


     void setClubManager(Long userId, Long clubId);

     void quashClubManager(Long userId, Long clubId);


}

