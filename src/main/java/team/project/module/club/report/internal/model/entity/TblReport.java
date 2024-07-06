package team.project.module.club.report.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TblReport {
    private Long id;
    private Boolean deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String uploader;
    private Long clubId;
    @TableField(value = "report_file_list", typeHandler = FastjsonTypeHandler.class)
    private Object reportFileList;
    private String reportType;
}
