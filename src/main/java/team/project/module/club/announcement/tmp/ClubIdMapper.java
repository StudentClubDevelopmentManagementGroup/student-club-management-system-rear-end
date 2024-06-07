package team.project.module.club.announcement.tmp;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import team.project.base.mapper.CrossModuleSQL;

import java.util.List;

@Mapper
public interface ClubIdMapper {

    @CrossModuleSQL("tbl_club")
    @Select("""
        SELECT tbl_club.id
        FROM   tbl_club
        WHERE  is_deleted = 0
        AND    tbl_club.name LIKE CONCAT('%', #{ clubName }, '%')
    """)
    List<Long> searchClubByName(String clubName);

    @CrossModuleSQL("tbl_club")
    @Select("""
        SELECT tbl_club.id
        FROM   tbl_club
        WHERE  is_deleted = 0
        AND    tbl_club.department_id = #{ departmentId }
    """)
    List<Long> searchClubByDepartmentId(long departmentId);
}
