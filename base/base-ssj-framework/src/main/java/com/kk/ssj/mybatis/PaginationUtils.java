package com.kk.ssj.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页页码生成工具类
 *  
 * @author wanjing
 * @date 2016-11-08
 * @version 1.0
 */
public class PaginationUtils {

	/**
	 * 生成分页页码
	 * @param pageMargin	 - 当前页的前后margin, 例如currentPage = 5,pageMargin = 2,则3,4,5,6,7这个五个页码是必须显示的
	 * @param currentPage	 - 当前页码
	 * @param totalPageCount - 总页数
	 * @return
	 */
	public static List<Integer> getPaginationItems(int pageMargin, int currentPage, int totalPageCount) {
		List<Integer> pageItems = new ArrayList<Integer>();
		if(pageMargin < 1){
			throw new IllegalArgumentException("'pageMargin' can not less than 1!");
		}
		if(currentPage < 1){
			throw new IllegalArgumentException("'currentPage' can not less than 1!");
		}
		if(totalPageCount < 0){
			throw new IllegalArgumentException("'totalPageCount' can not less than 0!");
		}
		if(totalPageCount < currentPage){
			return pageItems;
		}
		int start = currentPage - pageMargin;
		int end = currentPage + pageMargin;
		if(start <= 0){
			int offset = Math.abs(start) + 1;
			start = start + offset;
			end = end + offset;
		}
		if(end > totalPageCount){
			int offset = totalPageCount - end;
			end = end + offset;
			start = start + offset;
			start = start < 1 ? 1 : start;
		}
		for(int i = start; i <= end; i++){
			if(i == start && i > 1){ //first
				pageItems.add(1);
				if(i - 1 > 2){
					pageItems.add(null);
				}else if(i - 1 == 2){
					pageItems.add(i - 1);
				}
			}
			pageItems.add(i);
			if(i == end && i < totalPageCount){ //last
				if(totalPageCount - end > 2){
					pageItems.add(null);
				}else if(totalPageCount - end == 2){
					pageItems.add(totalPageCount - 1);
				}
				pageItems.add(totalPageCount);
			}
		}
		return pageItems;
	}
	
	public static List<Integer> getPaginationItems(Pager pager) {
		return getPaginationItems(pager.getPageMargin(), pager.getCurrentPage(), pager.getTotalPageCount());
	}
	
	/*public static void main(String[] args) {
		int pageMargin = 2;
		int totalPageCount = 10;
		for(int i = 1; i <= totalPageCount; i++){
			System.out.println("pageMargin = " + pageMargin + ", currentPage = " + i + ", totalPageCount = " + totalPageCount + ", pageItems = " + PaginationUtils.getPaginationItems(pageMargin, i, totalPageCount));
		}
	}*/

}
