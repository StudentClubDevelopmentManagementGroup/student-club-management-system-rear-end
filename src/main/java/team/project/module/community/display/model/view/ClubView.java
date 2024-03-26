package team.project.module.community.display.model.view;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ClubView {
    private Long id;
    private Long departmentId;
    private String name;
    private Integer state;


}
