package team.project.module.club.announcement.internal.model.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AnnSearchQO {
    private List<Long>   clubIdList;
    private List<String> authorIdList;
    private String       titleKeyword;
    private LocalDate    fromDate;
    private LocalDate    toDate;
}
