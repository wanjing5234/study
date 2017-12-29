package com.kk.ssj.resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kk.ssj.constant.ApiConfig;
import com.kk.ssj.exception.ApiException;
import com.kk.ssj.model.User;
import com.kk.ssj.mybatis.Pager;
import com.kk.ssj.mybatis.PaginationUtils;
import com.kk.ssj.response.ListResponse;
import com.kk.ssj.service.impl.user.UserService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("{apiVersion: v[0-9\\.]+}/users")
@RolesAllowed({ApiConfig.ROLE_USER, ApiConfig.ROLE_ADMIN})
public class UserResource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	@Autowired
	private UserService userService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ListResponse<User>  getAllUsers() {
		List<User> userList = userService.getUserList();
		ListResponse<User> listResponse = new ListResponse<>();
		listResponse.setItems(userList);
		return listResponse;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllUsers/{pageNum}/{searchType}")
	public ListResponse<Object> getAllUsers(@PathParam("pageNum") Integer pageNum, @PathParam("searchType") Integer searchType, @QueryParam("searchKey")String searchKey) {
		logger.info("{} {}", "getAllUsers", "传入参数 : pageNum = " + pageNum + ", searchType = " + searchType + ", searchKey = " + searchKey);
		ListResponse<Object> listResponse = new ListResponse<Object>();
		
		PageHelper.startPage(pageNum, Pager.DEFAULT_PAGE_SIZE);
		List<User> list = userService.getUserListBySearch(searchType, searchKey);
		
		PageInfo<User> pageInfo = new PageInfo<User>(list);
		List<Integer> pageList = PaginationUtils.getPaginationItems(Pager.DEFAULT_PAGE_MARGIN, pageNum, pageInfo.getPages());
		
		listResponse.addItem(list);
		listResponse.addItem(pageList);
		
		return listResponse;
	}

	@GET
	@Path("/getUserById/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ ApiConfig.ROLE_USER, ApiConfig.ROLE_ADMIN })
	public User getUserById(@PathParam("userId") int userId, @Context ContainerRequestContext crc) throws ApiException {
		logger.info("-------------> getUserById data = {} begin -------------", userId);
		if (crc != null && crc.getProperty("user-id") != null) {
			userId = (int) crc.getProperty("user-id");
		}
		User user = userService.getOneById(userId);
		logger.info("-------------> getUserById data = {} end -------------", user.toString());
		return user;
	}
	
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User createUser(@Valid User user) throws ApiException {
    	logger.info("-------------> createUser data = {} begin -------------", user.toString());
    	User userReturn = null;
    	try {
    		userReturn = userService.createUser(user);
		} catch (ApiException e) {
			throw e;
		}
    	logger.info("-------------> createUser end -------------");
		return userReturn;
	}

	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ ApiConfig.ROLE_USER, ApiConfig.ROLE_ADMIN })
	public User updateUser(@PathParam("userId") int userId, @Valid User userUpdater,@Context ContainerRequestContext crc) throws ApiException {
		logger.info("-------------------------> updateUser userId = {} , userUpdater = {} begin", userId, userUpdater.toString());
		User userReturn = null;
    	try {
    		 String optUserName = (String) crc.getProperty("user-username");
    		 userReturn = userService.updateUserByDuty(userId, userUpdater, optUserName);
		} catch (ApiException e) {
			throw e;
		}
    	logger.info("-------------> updateUser end -------------");
		return userReturn;
	}

	@DELETE
	@Path("/{userId}/{deleteDirectory}")
	@Produces(MediaType.APPLICATION_JSON)
	public User deleteUser(@PathParam("userId") int userId, @PathParam("deleteDirectory") boolean deleteDirectory)throws ApiException {
		logger.info("-------------------------> deleteUser userId = {} ", userId);
		User userReturn = null;
    	try {
    		userReturn = userService.deleteUser(userId, deleteDirectory);
		} catch (ApiException e) {
			throw e;
		}
    	
    	logger.info("-------------> deleteUser end -------------");
		return userReturn;
	}
	
	@DELETE
	@Path("/batchDeleteUsers/{usersList}/{deleteDirectory}")
    @Produces(MediaType.APPLICATION_JSON)
	public ListResponse<Object> batchDeleteUsers(@PathParam("usersList") String usersList,@PathParam("deleteDirectory") boolean deleteDirectory) throws ApiException {
		ListResponse<Object> listResponse = new ListResponse<Object>();
		logger.info("-------------------------> batchDeleteUsers usersList = {} ", usersList);
		List<Integer> usersIdList = new ArrayList<Integer>();
		JSONArray jsonArray = JSONArray.fromObject(usersList);// 把String转换为json
		usersIdList = JSONArray.toList(jsonArray, Integer.class);// 这里的t是Class<T>
		for (Integer userId : usersIdList) {
			try {
				userService.deleteUser(userId, deleteDirectory);
			} catch (ApiException e) {
				throw e;
			}
		}
		listResponse.addItem("success");
		return listResponse;
	}
	
}
