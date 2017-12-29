package com.kk.ssj.mybatis;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分页Pager对象
 * 
 * @author wanjing
 * @date 2016-11-08
 * @version 1.0
 */
public class Pager implements Serializable {

    private static final long serialVersionUID = -33585790212387399L;

    /**
     * 默认的当前页的前后margin
     */
    public static final int DEFAULT_PAGE_MARGIN = 2;
    
    /**
     * 默认每页显示多少条
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 默认每页显示多少条,可配
     */
    public static final String MATCH_PAGE_SIZE = "10";

    /**
     * 当前页码
     */
    private int currentPage = 1;

    /**
     * 每页显示多少条
     */
    private int pageSize = 10;

    /**
     * 查询总记录数
     */
    private int totalRowCount = 0;

    /**
     * 可分多少页
     */
    private int totalPageCount = 0;

    /**
     * 分页页码列表 例如: [1,2,3,4,5,null,10] 其中null代表省略号...
     */
    private transient List<Integer> pageItems;

    private transient int pageMargin = DEFAULT_PAGE_MARGIN;

    public Pager() {
        super();
    }

    public Pager(Integer currentPage, Integer pageSize) {
        super();
        if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
        }
        if (currentPage != null && currentPage > 0) {
            this.currentPage = currentPage;
        }
    }
    
    public Pager(int currentPage, int pageSize, int totalRowCount) {
    	super();
    	this.currentPage = currentPage;
    	this.pageSize = pageSize;
    	this.totalRowCount = totalRowCount;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(int totalpageInfoRowCount) {
        this.totalRowCount = totalpageInfoRowCount;
    }

    public int getTotalPageCount() {
        if (totalRowCount <= 0) {
            totalPageCount = 0;
        } else {
            totalPageCount = totalRowCount % pageSize == 0 ? totalRowCount / pageSize : (totalRowCount / pageSize) + 1;
        }
        return totalPageCount;
    }

    public List<Integer> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<Integer> pageItems) {
        this.pageItems = pageItems;
    }

    public int getPageMargin() {
        return pageMargin;
    }

    public void setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
    }

    public String toString() {
        return "Pager [currentPage=" + currentPage + ", pageSize=" + pageSize + ", totalRowCount=" + totalRowCount
                + ", totalPageCount=" + totalPageCount + "]";
    }

}
