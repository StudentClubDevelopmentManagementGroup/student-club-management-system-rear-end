package team.project.base.model.view;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/* 用来封装分页查询的返回结果 */
@Getter
@Setter
public class PageVO<VO> {
    @JsonProperty("records")      private List<VO> records;
    @JsonProperty("current_page") private Long     currentPage;
    @JsonProperty("page_size")    private Long     pageSize;
    @JsonProperty("total_pages")  private Long     totalPages;
    @JsonProperty("total_item")   private Long     totalItem;

    /* 如果分页查询得出的结果还需要进一步转换（例如，查出的是 DO，需要转换成 VO），则用这个构造器 */
    public PageVO(List<VO> records, Page<?> page) {
        this.records     = records;

        this.currentPage = page.getCurrent();
        this.pageSize    = page.getSize();

        if (page.searchCount()) {
            this.totalPages  = page.getPages();
            this.totalItem   = page.getTotal();
        }
    }

    /* 如果分页查询得出的结果不需要转换，则用这个构造器 */
    public PageVO(Page<VO> page) {
        this(page.getRecords(), page);
    }
}
