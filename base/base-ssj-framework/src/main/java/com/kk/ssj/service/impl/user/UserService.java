package com.kk.ssj.service.impl.user;

import com.kk.ssj.exception.ApiException;
import com.kk.ssj.model.User;

import java.util.List;

public interface UserService {

	public List<User> getUserList();
	public List<User> getUserListBySearch(int searchType, String searchKey);
	public User getOneById(String userId);
	public User getOneByUsername(String username);
	public User getOneById(int userId);
	public int save(User user);
	public int update(User user);
	public int deleteById(int userId);
	
	public User createUser(User user) throws ApiException;
	public User updateUser(int userId, User userUpdater) throws ApiException;
	public User updateUserByDuty(int userId, User userUpdater, String optUserName) throws ApiException;
	public User deleteUser(int userId, boolean deleteDirectory)throws ApiException;

}
