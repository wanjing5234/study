package com.kk.ssj.mybatis;

import java.util.List;

/**
 * Mybatis DAO接口
 *  
 * @author wanjing
 * @date 2016-11-08
 * @version 1.0
 */
public interface BaseMybatisDAO {
	
	/**
	 * 执行insert语句
	 * @param statementKey
	 * @param paramObj
	 * @return
	 */
	public int insert(String statementKey, Object paramObj);
	
	/**
	 * 执行jdbc批量insert语句
	 * @param <T>
	 * @param statementKey
	 * @param paramObjList
	 * @return
	 */
	public <T> int[] batchInsert(String statementKey, List<T> paramObjList);
	
	/**
	 * 执行update语句
	 * @param statementKey
	 * @param paramObj
	 * @return
	 */
	public int update(String statementKey, Object paramObj);
	
	/**
	 * 执行jdbc批量update语句
	 * @param <T>
	 * @param statementKey
	 * @param paramObjList
	 * @return
	 */
	public <T> int[] batchUpdate(String statementKey, List<T> paramObjList);
	
	/**
	 * 执行delete语句
	 * @param statementKey
	 * @param paramObj
	 * @return
	 */
	public int delete(String statementKey, Object paramObj);
	
	/**
	 * 执行jdbc批量delete语句
	 * @param <T>
	 * @param statementKey
	 * @param paramObjList
	 * @return
	 */
	public <T> int[] batchDelete(String statementKey, List<T> paramObjList);
	
	/**
	 * 执行select语句(返回条数)
	 * @param <T>
	 * @param statementKey
	 * @param paramObj
	 * @return
	 */
	public int selectCount(String statementKey, Object paramObj);
	
	/**
	 * 执行select语句(至多返回一条记录)
	 * @param <T>
	 * @param statementKey
	 * @param paramObj
	 * @return
	 */
	public <T> T selectOne(String statementKey, Object paramObj);
	
	/**
	 * 执行select语句(至多返回一条记录)
	 * @param <T>
	 * @param statementKey
	 * @param paramObj
	 * @param escapeFilter
	 * @return
	 */
	public <T> T selectOne(String statementKey, Object paramObj, EscapeFilter<T> escapeFilter);
	
	/**
	 * 执行select语句(可返回多条记录)
	 * @param <T>
	 * @param statementKey
	 * @param paramObj
	 * @return
	 */
	public <T> List<T> selectList(String statementKey, Object paramObj);
	
	/**
	 * 执行select语句(可返回多条记录)
	 * @param <T>
	 * @param statementKey
	 * @param paramObj
	 * @param escapeFilter
	 * @return
	 */
	public <T> List<T> selectList(String statementKey, Object paramObj, EscapeFilter<T> escapeFilter);
	
	/**
	 * 执行select语句(可返回多条记录)(分页使用)
	 * @param <T>
	 * @param statementKey
	 * @param paramObj
	 * @param pager
	 * @return
	 */
	public <T> PageList<T> selectList(String statementKey, Object paramObj, Pager pager);
	
	/**
	 * 执行select语句(可返回多条记录)(分页使用)
	 * @param <T>
	 * @param statementKey
	 * @param paramObj
	 * @param escapeFilter
	 * @param pager
	 * @return
	 */
	public <T> PageList<T> selectList(String statementKey, Object paramObj, EscapeFilter<T> escapeFilter, Pager pager);
	
	/**
	 * 以暴露SqlSession的方式执行数据库操作
	 * @param <T>
	 * @param action
	 * @return
	 */
	public <T> T execute(SqlSessionCallback<T> action);
	
}
