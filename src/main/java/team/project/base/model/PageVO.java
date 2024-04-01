package team.project.base.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/* 用来封装分页查询的返回结果 */
@Data
public class PageVO<VO> {
    @JsonProperty("records")      private List<VO> records;
    @JsonProperty("current_page") private Long     currentPage;
    @JsonProperty("page_size")    private Long     pageSize;
    @JsonProperty("total_pages")  private Long     totalPages;
    @JsonProperty("total_item")   private Long     totalItem;

    public PageVO(List<VO> records, Page<?> page) {
        this.records     = records;

        this.currentPage = page.getCurrent();
        this.pageSize    = page.getSize();

        if (page.searchCount()) {
            this.totalPages  = page.getPages();
            this.totalItem   = page.getTotal();
        }
    }
}
