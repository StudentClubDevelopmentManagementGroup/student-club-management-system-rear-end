package team.project.base.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/* 用来封装分页查询的请求 */
@Getter
@Setter
public class PagingQueryReq {
    @Min(value=1, message="分页查询指定当前页码不能小于 1")
    @JsonProperty("page_num")
    private Long pageNum;

    @Min(value=1, message="每页大小最小不能小于 1")
    @JsonProperty("page_size")
    private Long pageSize;
}
