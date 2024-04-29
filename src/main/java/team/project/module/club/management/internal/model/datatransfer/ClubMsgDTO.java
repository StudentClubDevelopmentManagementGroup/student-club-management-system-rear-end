package team.project.module.club.management.internal.model.datatransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import team.project.module.club.management.internal.controller.TblClubController;
import team.project.module.club.management.internal.mapper.TblClubMapper;
import team.project.module.club.management.internal.model.request.ListClubInfoReq;

/**
 * <p>此 DTO 用在多处：
 * <li>用作接收数据库的查询结果，包括但不限于： {@link TblClubMapper#findAll}</li>
 * <li>用作封装返回给前端的响应： {@link TblClubController#selectClub(ListClubInfoReq)}</li>
 * </p>
 * <p>此类承担了过多的任务，用法上未遵守单一职责。如果要修改此类，请务必多留心</p>
 * */
@Data
public class ClubMsgDTO {
    @JsonProperty("name")            String   name;
    @JsonProperty("department_name") String   departmentName;
    @JsonProperty("department_id")   Long     departmentId;
    @JsonProperty("club_id")         Long     clubId;
    @JsonProperty("number")          Integer  number = 0;
    @JsonProperty("state")           Boolean  state;
    @JsonProperty("is_deleted")      Boolean  deleted;
    @JsonProperty("manager")         String   manager;
}
