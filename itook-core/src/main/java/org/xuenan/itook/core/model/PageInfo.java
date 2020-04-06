package org.xuenan.itook.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.xuenan.itook.core.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageInfo<T> extends SysModel {
    private static final long serialVersionUID = 1L;
    private static final int num = 6;
    private int pageNo = 1;
    private int pageSize = 10;
    private long total = 0L;
    private List<T> list = Arrays.asList();

    public Integer getTotalPage() {
        double totalPage = (double)this.total / ((double)this.pageSize * 1.0D);
        return (int)Math.ceil(totalPage);
    }

    public PageInfo() {
    }

    public PageInfo(Integer pageNo, Integer pageSize, long total, List<T> list) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    public <M> PageInfo<M> convert(PageInfo.PageConvert<T, M> convert) {
        convert.getClass();
        List<M> listM = ListUtils.n(this.list).list(convert::convert).to();
        return new PageInfo(this.pageNo, this.pageSize, this.total, listM);
    }

    @JsonIgnore
    public List<Integer> getPageNos() {
        return this.getPageNos(6);
    }

    @JsonIgnore
    public List<Integer> getPageNos(int num) {
        int totalPage = this.getTotalPage();
        if (totalPage < this.pageNo) {
            return new ArrayList(0);
        } else {
            ArrayList nums;
            int i;
            if (num >= totalPage) {
                nums = new ArrayList(totalPage);

                for(i = 0; i < totalPage; ++i) {
                    nums.add(i + 1);
                }

                return nums;
            } else if (this.pageNo <= num / 2) {
                nums = new ArrayList(num);

                for(i = 0; i < num; ++i) {
                    nums.add(i + 1);
                }

                return nums;
            } else if (totalPage < this.pageNo + num / 2) {
                nums = new ArrayList(num);

                for(i = totalPage - num; i < totalPage; ++i) {
                    nums.add(i + 1);
                }

                return nums;
            } else {
                nums = new ArrayList(num);

                for(i = -num / 2; i < num / 2; ++i) {
                    nums.add(this.pageNo + i + 1);
                }

                return nums;
            }
        }
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return (List)(this.list == null ? new ArrayList() : this.list);
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @FunctionalInterface
    public interface PageConvert<T, M> {
        M convert(T t);
    }
}
