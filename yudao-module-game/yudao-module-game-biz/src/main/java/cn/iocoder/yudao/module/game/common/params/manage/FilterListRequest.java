package cn.iocoder.yudao.module.game.common.params.manage;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class FilterListRequest {

    @Parameter(name = "页码", required = true)
    private Integer pageNum;

    @Parameter(name = "页容量", required = true)
    private Integer pageSize;

    @Schema(description  = "排序字段")
    private String sortBy;

    @Schema(description  = "排序顺序")
    private String order;

    @Schema(hidden = true)
    private String orderBy;

    public FilterListRequest() {
        super();
    }

    /**
     * 初始化.
     */
    public FilterListRequest(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        if (Objects.nonNull(order)) {
            this.orderBy = sortBy + " " + order;
        } else {
            this.orderBy = sortBy;
        }
    }

    /**
     * 初始化.
     */
    public FilterListRequest(int pageNum, int pageSize, String sortBy, String order) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.order = order;
        if (Objects.nonNull(order) && !order.trim().equals("")) {
            this.orderBy = sortBy + " " + order;
        } else {
            this.orderBy = sortBy;
        }
    }

    @Override
    public String toString() {
        return "FilterListRequest{"
            + "pageNum=" + pageNum
            + ", pageSize=" + pageSize
            + ", sortBy='" + sortBy + '\''
            + ", order='" + order + '\''
            + '}';
    }

    /**
     * 在此直接执行分页, 如果sortby为传值，则不排序.
     */
//    public Page startPage() {
//        return PageUtils.startPage(pageNum, pageSize, orderBy);
//    }
}

