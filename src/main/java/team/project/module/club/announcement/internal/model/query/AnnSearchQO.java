package team.project.module.club.announcement.internal.model.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnnSearchQO {
    private Long      clubId;
    private String    titleKeyword;
    private LocalDate fromDate;
    private LocalDate toDate;
}
