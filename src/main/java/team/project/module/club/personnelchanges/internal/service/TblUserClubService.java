package team.project.module.club.personnelchanges.internal.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.project.module.club.management.internal.model.entity.TblUserClubDO;


@Service
public interface TblUserClubService extends IService<TblUserClubDO> {


     void setClubManager(String userId, Long clubId);

     void quashClubManager(String userId, Long clubId);


}

