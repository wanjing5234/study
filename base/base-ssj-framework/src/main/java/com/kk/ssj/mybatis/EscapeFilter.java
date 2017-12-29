package com.kk.ssj.mybatis;
/**
 * 针对对象的某个属性的"值"与"义"的转义接口，如
 * public class CmsUser {
 * 		private String userType;
 * 
 * 		private String userTypeName;
 * }
 * 对于从数据库中查出来的User对象可能仅仅是userType=0,1,2这样的代码所表示的常量值,
 * 而用于展示在页面上的字段userTypeName却是没有赋值,
 * 该EscapeFilter接口即为解决此类情况下的"值"与"义"的转换功能的
 * @param <T>
 * @author wanjing
 * @date 2016-11-08
 * @version 1.0
 */
public interface EscapeFilter<T> {

	public void doEscapeFilter(T element);
	
}
