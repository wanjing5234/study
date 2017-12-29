package com.kk.ssj.mybatis;

import org.apache.ibatis.session.SqlSession;

/**
 * SqlSession callback
 * 
 * @author wanjing
 * @date 2016-11-08
 * @version 1.0
 */
public interface SqlSessionCallback<T> {

	public T doInSqlSession(SqlSession sqlSession);

}

