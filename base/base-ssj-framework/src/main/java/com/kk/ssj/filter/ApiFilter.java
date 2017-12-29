package com.kk.ssj.filter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.kk.ssj.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ApiFilter implements ContainerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(ApiFilter.class);
	
	@Context
	private ResourceInfo resourceInfo;
	
    @Context
    private HttpServletRequest request;
    
    @Override
    public void filter(ContainerRequestContext requestContext) {
    	//logger.info("-------------ApiFilter begin------------");
    	String url = request.getRequestURI();
    	//如果是gateway过来的，进行过滤，否则放行
        if (url.matches(Constant.API_URL_FILTER)) {

		}
        //logger.info("-------------ApiFilter end------------");
    }

}
