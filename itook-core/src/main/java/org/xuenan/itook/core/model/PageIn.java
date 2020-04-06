package org.xuenan.itook.core.model;

public class PageIn extends SysModel{
    private static final long serialVersionUID = 1L;
    private Integer pageNo = 1;
    private Integer pageSize = 10;

    public PageIn() {
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if (pageNo != null) {
            this.pageNo = pageNo;
        }

    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize != null) {
            this.pageSize = pageSize;
        }

    }
}
