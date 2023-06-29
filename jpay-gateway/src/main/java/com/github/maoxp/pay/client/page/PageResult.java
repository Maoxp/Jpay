package com.github.maoxp.pay.client.page;

import java.io.Serializable;
import java.util.List;

/**
 * PageResult
 *
 * @author mxp
 * @since JDK 1.8
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1237799054856218404L;

    private Long total;

    private int page = 1;

    private int pageSize = 20;

    private List<T> rows;

    public PageResult() {
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult(Long total, List<T> rows, int page) {
        this.page = page;
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
