package org.xhome.xauth.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhome.common.constant.Status;
import org.xhome.util.StringUtils;
import org.xhome.web.util.RequestUtils;
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.AuthLogService;
import org.xhome.xauth.core.service.UserService;
import org.xhome.xauth.web.util.AuthUtils;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Mar 7, 2014
 * @describe
 */
@Component
public class AuthFilter implements Filter {

    @Autowired
    private UserService           userService;
    @Autowired
    private AuthLogService        authLogService;

    private Logger                logger      = LoggerFactory.getLogger(AuthFilter.class);
    private Map<String, String[]> authorities = new HashMap<String, String[]>();

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            Properties properties = new Properties();
            InputStream resource = Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream("authority.properties");
            if (resource != null) {
                properties.load(resource);

                Set<Object> keys = properties.keySet();
                String uri, roleName;
                String[] roleNames;
                List<String> tmps;
                for (Object key : keys) {
                    uri = (String) key;
                    roleNames = properties.getProperty(uri).split(",");
                    logger.debug("load auth filter config {}={}", uri,
                                    roleNames);

                    tmps = new ArrayList<String>();
                    for (int i = 0; i < roleNames.length; i++) {
                        roleName = roleNames[i];
                        if (StringUtils.isNotEmpty(roleName)) {
                            tmps.add(roleName);
                        }
                    }
                    if (tmps.size() > 0) {
                        roleNames = new String[tmps.size()];
                        tmps.toArray(roleNames);
                        authorities.put(uri, roleNames);
                    }
                }
            } else {
                logger.warn("don't config authority.properties");
            }
        } catch (IOException e) {
            logger.error("权限配置文件加载失败", e);
        }
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                    FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        User user = AuthUtils.getCurrentUser(request);

        if (AuthUtils.isAnonymousUser(user) && !AuthUtils.isUserLogout(request)) {
            // 自动登录
            Cookie[] cookies = request.getCookies();
            String name = null, password = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ((name != null) && (password != null)) break;
                    if (AuthUtils.USER_COOKIE_NAME.equals(cookie.getName())) {
                        name = cookie.getValue();
                    } else if (AuthUtils.USER_COOKIE_PASSWORD.equals(cookie
                                    .getName())) {
                        password = cookie.getValue();
                    }
                }
            }
            if ((name != null) && (password != null)) {
                User u = userService.getUser(user, name);
                if (u != null && u.getPassword().equals(password)) {
                    logger.info("auto login for user [{}]", name);
                    user = u;
                    AuthUtils.setCurrentUser(request, user);
                    AuthLog authLog = new AuthLog(
                                    user,
                                    "COOKIE",
                                    RequestUtils.getRequestAddress(request),
                                    RequestUtils.getRequestUserAgent(request),
                                    RequestUtils.getRequestUserAgentName(request));
                    authLog.setStatus(Status.SUCCESS);
                    authLogService.logAuth(authLog);
                }
            }
        }

        boolean allow = true; // 默认允许所有请求
        String uri = RequestUtils.getRequestURI(request);
        if (StringUtils.isNotEmpty(uri)) {
            for (Entry<String, String[]> entry : authorities.entrySet()) {
                if (uri.matches(entry.getKey())) {
                    allow = false;
                    // 拒绝匿名用户访问
                    if (AuthUtils.isAnonymousUser(user)) {
                        break;
                    }
                    for (String roleName : entry.getValue()) {
                        if (user.hasRole(roleName)) {
                            allow = true;
                            break;
                        }
                    }
                    break;
                }
            }
        }

        if (allow) {
            logger.info("允许用户" + user.getName() + "访问：" + uri);
            chain.doFilter(req, res);
        } else {
            logger.info("拒绝用户" + user.getName() + "访问：" + uri);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        authorities.clear();
    }

    /**
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * @param userService
     *            the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return the authLogService
     */
    public AuthLogService getAuthLogService() {
        return authLogService;
    }

    /**
     * @param authLogService
     *            the authLogService to set
     */
    public void setAuthLogService(AuthLogService authLogService) {
        this.authLogService = authLogService;
    }

}
