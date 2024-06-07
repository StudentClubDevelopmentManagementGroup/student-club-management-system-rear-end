package team.project.module.club.announcement.internal.model.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
public class AnnSearchQO {
    private Collection<Long>   clubIdColl;
    private Collection<String> authorIdColl;
    private String             titleKeyword;
    private LocalDate          fromDate;
    private LocalDate          toDate;
}
