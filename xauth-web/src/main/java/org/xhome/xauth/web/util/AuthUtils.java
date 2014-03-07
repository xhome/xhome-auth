package org.xhome.xauth.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xhome.common.Base;
import org.xhome.web.util.SessionUtils;
import org.xhome.xauth.User;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description
 */
public class AuthUtils {

	public final static String AUTHCODE_SESSION_KEY = "org.xhome.xauth.session.authcode";
	public final static String USER_SESSION_KEY = "org.xhome.xauth.session.user";
	public final static String USER_LOGOUT_SESSION_KEY = "org.xhome.xauth.session.user.logout";

	public final static String USER_COOKIE_PATH = "/";
	public final static String USER_COOKIE_NAME = "org.xhome.xuth.cookie.user.name";
	public final static String USER_COOKIE_PASSWORD = "org.xhome.xauth.cookie.user.password";

	private final static User anonymousUser = new User("Anonymous");

	static {
		anonymousUser.setId(0L);
	}

	/**
	 * 获取当前HttpServletRequest对象
	 * 
	 * @return
	 */
	public static HttpServletRequest getServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		return getCurrentUser(getServletRequest());
	}

	/**
	 * 获取已登录用户
	 * 
	 * @param request
	 * @return
	 */
	public static User getCurrentUser(HttpServletRequest request) {
		User user = (User) SessionUtils.getSessionAttribute(request,
				USER_SESSION_KEY);
		return user == null ? anonymousUser : user;
	}

	/**
	 * 设置当前登录用户
	 * 
	 * @param user
	 */
	public static void setCurrentUser(User user) {
		SessionUtils.setSessionAttribute(getServletRequest(), USER_SESSION_KEY,
				user);
	}

	/**
	 * 设置已登录用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void setCurrentUser(HttpServletRequest request, User user) {
		SessionUtils.setSessionAttribute(request, USER_SESSION_KEY, user);
	}

	/**
	 * 判断是否为匿名用户
	 * 
	 * @param user
	 * @return
	 */
	public static boolean isAnonymousUser(User user) {
		return anonymousUser.equals(user);
	}

	/**
	 * 判断当前用户是否为匿名用户
	 * 
	 * @return
	 */
	public static boolean isAnonymousUser() {
		return isAnonymousUser(getCurrentUser());
	}

	/**
	 * 获取匿名用户
	 * 
	 * @return
	 */
	public static User getAnonymousUser() {
		return anonymousUser;
	}

	/**
	 * 判断用户是否已退出登录
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isUserLogout(HttpServletRequest request) {
		Object r = request.getSession().getAttribute(USER_LOGOUT_SESSION_KEY);
		return r != null && (Boolean) r;
	}

	/**
	 * 判断用户是否已退出登录
	 * 
	 * @return
	 */
	public static boolean isUserLogout() {
		return isUserLogout(getServletRequest());
	}

	/**
	 * 从Session中移除当前登录用户
	 */
	public static void removeCurrentUser() {
		removeCurrentUser(getServletRequest());
	}

	/**
	 * 从Session中移除登录用户
	 * 
	 * @param request
	 */
	public static void removeCurrentUser(HttpServletRequest request) {
		SessionUtils.removeSessionAttribute(request, USER_SESSION_KEY);
		SessionUtils
				.setSessionAttribute(request, USER_LOGOUT_SESSION_KEY, true);
	}

	/**
	 * 在Cookie中记录用户信息； 有效期为90天；Cookie路径为： /
	 * 
	 * @param response
	 * @param user
	 *            用户信息
	 */
	public static void setCookieUser(HttpServletResponse response, User user) {
		setCookieUser(response, user, 7776000);
	}

	/**
	 * 在Cookie中记录用户信息； Cookie路径为： /
	 * 
	 * @param response
	 * @param user
	 *            用户信息
	 * @param maxAge
	 *            Cookie 有效期
	 */
	public static void setCookieUser(HttpServletResponse response, User user,
			int maxAge) {
		setCookieUser(response, user, maxAge, USER_COOKIE_PATH);
	}

	/**
	 * 在Cookie中记录用户信息
	 * 
	 * @param response
	 * @param user
	 *            用户信息
	 * @param maxAge
	 *            Cookie 有效期
	 * @param path
	 *            Cookie 路径
	 */
	public static void setCookieUser(HttpServletResponse response, User user,
			int maxAge, String path) {
		Cookie cookie = new Cookie(AuthUtils.USER_COOKIE_NAME, user.getName());
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
		cookie = new Cookie(AuthUtils.USER_COOKIE_PASSWORD, user.getPassword());
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	/**
	 * 获取Cookie中的用户信息； Cookie路径为： /
	 * 
	 * @param request
	 * @return
	 */
	public static User getCookieUser(HttpServletRequest request) {
		return getCookieUser(request, USER_COOKIE_PATH);
	}

	/**
	 * 获取Cookie中的用户信息
	 * 
	 * @param request
	 * @param path
	 *            Cookie路径
	 * @return
	 */
	public static User getCookieUser(HttpServletRequest request, String path) {
		Cookie[] cookies = request.getCookies();
		String cookieName = null;
		User user = new User();
		boolean findName = false, findPassword = false;
		for (Cookie cookie : cookies) {
			if (cookie.getPath().equals(path)) {
				cookieName = cookie.getName();
				if (AuthUtils.USER_COOKIE_NAME.equals(cookieName)) {
					user.setName(cookie.getValue());
					findName = true;
				} else if (AuthUtils.USER_COOKIE_PASSWORD.equals(cookieName)) {
					user.setPassword(cookie.getValue());
					findPassword = true;
				}
				if (findName && findPassword) {
					break;
				}
			}
		}
		return (findName && findPassword) ? user : null;
	}

	/**
	 * 在Cookie中删除用户信息； Cookie路径为： /
	 * 
	 * @param response
	 * @param user
	 *            用户信息
	 */
	public static void removeCookieUser(HttpServletResponse response, User user) {
		removeCookieUser(response, user, USER_COOKIE_PATH);
	}

	/**
	 * 在Cookie中删除用户信息
	 * 
	 * @param response
	 * @param user
	 *            用户信息
	 * @param path
	 *            Cookie 路径
	 */
	public static void removeCookieUser(HttpServletResponse response,
			User user, String path) {
		setCookieUser(response, user, 0, path);
	}

	/**
	 * 设置对象的所有者为当前登录用户
	 * 
	 * @param obj
	 */
	public static void setOwner(Base obj) {
		setOwner(getServletRequest(), obj);
	}

	/**
	 * 设置对象的所有者
	 * 
	 * @param obj
	 */
	public static void setOwner(HttpServletRequest request, Base obj) {
		User user = getCurrentUser(request);
		obj.setOwner(user.getId());
		obj.setModifier(user.getId());
	}

	/**
	 * 设置对象的修改者为当前登录用户
	 * 
	 * @param obj
	 */
	public static void setModifier(Base obj) {
		setModifier(getServletRequest(), obj);
	}

	/**
	 * 设置对象的修改者
	 * 
	 * @param obj
	 */
	public static void setModifier(HttpServletRequest request, Base obj) {
		User user = getCurrentUser(request);
		obj.setModifier(user.getId());
	}

}
