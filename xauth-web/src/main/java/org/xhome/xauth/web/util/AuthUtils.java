package org.xhome.xauth.web.util;

import javax.servlet.http.HttpServletRequest;
import org.xhome.common.Base;
import org.xhome.http.util.SessionUtils;
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
	
	public final static String USER_COOKIE_NAME = "org.xhome.xuth.cookie.user.name";
	public final static String USER_COOKIE_PASSWORD = "org.xhome.xauth.cookie.user.password";
	
	private final static User anonymousUser = new User("Anonymous");
	
	static {
		anonymousUser.setId(0L);
	}
	
	public static User getCurrentUser(HttpServletRequest request) {
		User user = (User) SessionUtils.getSessionAttribute(request, USER_SESSION_KEY);
		return user == null ? anonymousUser : user;
	}

	public static void setCurrentUser(HttpServletRequest request, User user) {
		SessionUtils.setSessionAttribute(request, USER_SESSION_KEY, user);
	}
	
	public static boolean isAnonymousUser(User user) {
		return anonymousUser.equals(user);
	}
	
	public static User getAnonymousUser() {
		return anonymousUser;
	}
	
	public static void removeCurrentUser(HttpServletRequest request) {
		SessionUtils.removeSessionAttribute(request, USER_SESSION_KEY);
	}

	public static void setOwner(HttpServletRequest request, Base obj) {
		User user = getCurrentUser(request);
		obj.setOwner(user.getId());
		obj.setModifier(user.getId());
	}

	public static void setModifier(HttpServletRequest request, Base obj) {
		User user = getCurrentUser(request);
		obj.setModifier(user.getId());
	}

}
