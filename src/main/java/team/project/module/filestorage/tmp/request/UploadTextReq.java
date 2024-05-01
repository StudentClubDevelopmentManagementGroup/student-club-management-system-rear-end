package team.project.module.filestorage.tmp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadTextReq {

 /* @JsonProperty("overwrite")
    String fileId; */

    @NotBlank(message="未指定标题")
    @JsonProperty("title")
    String title;

    @NotBlank(message="未指定内容")
    @JsonProperty("content")
    String content;
}
