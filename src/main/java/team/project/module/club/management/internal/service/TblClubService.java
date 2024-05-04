package team.project.module.club.management.internal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import team.project.base.model.view.PageVO;
import team.project.module.club.management.internal.model.datatransfer.ClubMsgDTO;
import team.project.module.club.management.internal.model.entity.TblClubDO;
import team.project.module.club.management.internal.model.query.ClubInfoQO;

@Service
public interface TblClubService extends IService<TblClubDO> {


     void createClub(Long departmentId, String name);

     /* review by ljh 2024-04-29 TODO
         下述三个 select 函数可以合为一个，因为它们的入参出参都是一样的
         落实到 SQL 时可以使用 if 标签动态增加 where 查询的条件
     */
     PageVO<TblClubDO> selectByNameAndDepartmentId(ClubInfoQO page);

     PageVO<TblClubDO> selectByName(ClubInfoQO page);

     PageVO<TblClubDO> selectByDepartmentId(ClubInfoQO page);

     void deleteClub(Long departmentId, String name);

     void reuseClub(Long departmentId, String name);

     void deactivateClub(Long departmentId, String name);

     void recoverClub(Long departmentId, String name);

     /* review TODO
         下述四个 findAll 函数可以合为一个，因为它们的入参出参都是一样的
         落实到 SQL 时可以使用 if 标签动态增加 where 查询的条件
     */

     PageVO<ClubMsgDTO> findAll(ClubInfoQO page);

     PageVO<ClubMsgDTO> findAllByDepartmentId(ClubInfoQO page);

     PageVO<ClubMsgDTO> findAllByName(ClubInfoQO page);

     PageVO<ClubMsgDTO> findAllByDepartmentIdAndName(ClubInfoQO page);
}
