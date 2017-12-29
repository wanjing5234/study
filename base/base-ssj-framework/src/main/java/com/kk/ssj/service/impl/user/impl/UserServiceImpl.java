package com.kk.ssj.service.impl.user.impl;

import com.kk.ssj.dao.UserDao;
import com.kk.ssj.exception.ApiException;
import com.kk.ssj.model.User;
import com.kk.ssj.mybatis.MybatisUtils;
import com.kk.ssj.service.impl.BaseServiceImpl;
import com.kk.ssj.service.impl.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<User> getUserList() {
		return userDao.selectList(MybatisUtils.getMapperKey(User.class, "selectUserList"), null);
	}
	
	@Override
	public List<User> getUserListBySearch(int searchType, String searchKey) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("searchType", searchType);
		return userDao.selectList(MybatisUtils.getMapperKey(User.class, "getUserListBySearch"), map);
	}
	
	@Override
	public User getOneById(String userId) {
		int uId = Integer.parseInt(userId);
		return this.getOneById(uId);
	}
	
	@Override
	public User getOneById(int userId) {
		return userDao.selectOne(MybatisUtils.getMapperKey(User.class, "selectOneById"), userId);
	}

	@Override
	public User getOneByUsername(String username) {
		return userDao.selectOne(MybatisUtils.getMapperKey(User.class, "selectOneByUsername"), username);
	}
	
	@Override
	public int save(User user) {
		return userDao.insert(MybatisUtils.getMapperKey(User.class, "save"), user);
	}

	@Override
	public int update(User user) {
		return userDao.update(MybatisUtils.getMapperKey(User.class, "update"), user);
	}

	@Override
	public int deleteById(int userId) {
		return userDao.update(MybatisUtils.getMapperKey(User.class, "deleteById"), userId);
	}
	
	@Override
	public User createUser(User user) throws ApiException {
		logger.info("{} {}", "createUser", user.toString());

		return user;
	}


	@Override
	public User updateUserByDuty(int userId, User userUpdater, String userName) throws ApiException {


		return null;
	}

	@Override
	public User updateUser(int userId, User userUpdater) throws ApiException {
		logger.info("-------------------------> updateUser userId = {} , userUpdater = {} begin", userId, userUpdater.toString());

		return userUpdater;
	}
	
	@Override
	public User deleteUser(int userId, boolean deleteDirectory)throws ApiException {
		logger.info("-------------------------> deleteUser userId = {} ", userId);
		User user = getOneById(userId);

		
		return user;
	}
	
}
