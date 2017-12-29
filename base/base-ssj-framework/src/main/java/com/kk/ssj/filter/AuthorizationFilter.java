package com.kk.ssj.filter;

import com.kk.ssj.constant.Constant;
import com.kk.ssj.model.User;
import com.kk.ssj.response.FlexSafeResult;
import com.kk.ssj.service.impl.user.UserService;
import com.kk.ssj.util.I18nUtil;
import org.glassfish.jersey.internal.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This filter verify the access permissions for a user based on username and
 * passowrd provided in request
 *
 * @author declan
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Context
    private ResourceInfo resourceInfo;

    @Autowired
    private UserService userService;

    @Context
    private HttpServletRequest request;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    // 创建线程
    public static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        //logger.info("-------------AuthorizationFilter begin------------");

        // Let OPTIONS request go. for CORS request.
        // Maybe we should delete this before release.
        if (requestContext.getMethod().equals("OPTIONS")) {
            return;
        }

        String url = request.getRequestURI();
        //如果是gateway过来的，进行放行，不过滤
        if (url.matches(Constant.API_URL_FILTER)) {
            return;
        }

        Method method = resourceInfo.getResourceMethod();
        Class cls = resourceInfo.getResourceClass();

        boolean permitAll = method.isAnnotationPresent(PermitAll.class);
        boolean denyAll = method.isAnnotationPresent(DenyAll.class);
        Set<String> rolesSet = null;

        if (!denyAll && !permitAll) {
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));
            } else {
                denyAll = cls.isAnnotationPresent(DenyAll.class);
                permitAll = cls.isAnnotationPresent(PermitAll.class);
                if (!permitAll && !denyAll && cls.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = (RolesAllowed) cls.getAnnotation(RolesAllowed.class);
                    rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));
                }
            }
        }

        if (permitAll) {
            return;
        }

        if (denyAll || rolesSet == null) {
            final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Access Forbidden")
                    .build();
            requestContext.abortWith(ACCESS_FORBIDDEN);
            return;
        }

        //Get request headers
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();

        //Fetch authorization header
        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

        final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.TEXT_PLAIN)
                .entity("Access Denied")
                .build();


        //If no authorization information present; block access
        if (authorization == null || authorization.isEmpty()) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        //Get encoded username and password
        final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        if (!tokenizer.hasMoreTokens()) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }
        final String username = tokenizer.nextToken();
        if (!tokenizer.hasMoreTokens()) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        User user = userService.getOneByUsername(username);
        if (null == user) {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        int languageSetting = 1; //默认值
        request.getSession().setAttribute("language",languageSetting);
        threadLocalRequest.set(request);

    }

}

