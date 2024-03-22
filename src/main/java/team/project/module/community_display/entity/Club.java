package team.project.module.community_display.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Club {
    private Long id;
    private int is_deleted;
    private Date create_time;
    private Date update_time;
    private Long department_id;
    private String name;
    private int state;



}
