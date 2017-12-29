package com.kk.ssj.mybatis;

import java.util.List;

/**
 * 分页查询结果对象
 * 
 * @param <T>
 * @author wanjing
 * @date 2016-11-08
 * @version 1.0
 */
public class PageList<T> {

	private List<T> dataList;
	
	private Pager pager;

	public PageList() {
		super();
	}

	public PageList(List<T> dataList, Pager pager) {
		super();
		this.dataList = dataList;
		this.pager = pager;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String toString() {
		return "PageList [dataList=" + dataList + ", pager=" + pager + "]";
	}
}
