package org.xhome.xauth.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xhome.common.Base;
import org.xhome.web.util.ServletContextUtils;
import org.xhome.web.util.SessionUtils;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description
 */
public class AuthUtils {

    // 存放在Session中的验证码KEY
    public final static String AUTHCODE_SESSION_KEY                 = "org.xhome.xauth.session.authcode";
    // 存放在Session中的当前登录用户信息KEY
    public final static String USER_SESSION_KEY                     = "org.xhome.xauth.session.user";
    // 存在在Session中的当前用户退出标记KEY
    public final static String USER_LOGOUT_SESSION_KEY              = "org.xhome.xauth.session.user.logout";
    // 存放在Session中的当前申请重置密码的用户信息KEY
    public final static String USER_RESET_PASSWORD_SESSION_KEY      = "org.xhome.xauth.session.user.reset.password";
    // 存在在Session中的当前申请重置密码的用户信息存放在ServletContext中的键值KEY（其值与发往用户邮箱的hash一致）
    public final static String USER_RESET_PASSWORD_HASH_SESSION_KEY = "org.xhome.xauth.session.user.reset.password.hash";

    // Cookie存放路径
    public final static String USER_COOKIE_PATH                     = "/";
    // 存放在Cookie中的用户名KEY
    public final static String USER_COOKIE_NAME                     = "org.xhome.xauth.cookie.user.name";
    // 存放在Cookie中的用户密码KEY
    public final static String USER_COOKIE_PASSWORD                 = "org.xhome.xauth.cookie.user.password";

    private final static User  anonymousUser                        = new User(
                                                                                    "Anonymous");

    static {
        anonymousUser.setId(0L);
        anonymousUser.addRole(new Role("anonymous"));
    }

    /**
     * 获取当前HttpServletRequest对象
     * 
     * @return
     */
    public static HttpServletRequest getServletRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ra != null ? ((ServletRequestAttributes) ra).getRequest() : null;
    }

    /**
     * 获取当前登录用户
     * 
     * @return
     */
    public static User getCurrentUser() {
        HttpServletRequest request = getServletRequest();
        return request != null ? getCurrentUser(request) : null;
    }

    /**
     * 获取已登录用户
     * 
     * @param session
     * @return
     */
    public static User getCurrentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        User user = (User) session.getAttribute(USER_SESSION_KEY);
        return user == null ? anonymousUser : user;
    }

    /**
     * 获取已登录用户
     * 
     * @param request
     * @return
     */
    public static User getCurrentUser(HttpServletRequest request) {
        return request != null ? getCurrentUser(request.getSession()) : null;
    }

    /**
     * 设置当前登录用户
     * 
     * @param user
     */
    public static void setCurrentUser(User user) {
        HttpServletRequest request = getServletRequest();
        if (request != null) {
            request.getSession().setAttribute(USER_SESSION_KEY, user);
        }
    }

    /**
     * 设置已登录用户
     * 
     * @param session
     * @param user
     */
    public static void setCurrentUser(HttpSession session, User user) {
        if (session != null) {
            session.setAttribute(USER_SESSION_KEY, user);
        }
    }

    /**
     * 设置已登录用户
     * 
     * @param request
     * @param user
     */
    public static void setCurrentUser(HttpServletRequest request, User user) {
        if (request != null) {
            setCurrentUser(request.getSession(), user);
        }
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
        User user = getCurrentUser();
        return user != null ? isAnonymousUser(user) : true;
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
    public static boolean isUserLogout(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object r = session.getAttribute(USER_LOGOUT_SESSION_KEY);
        return r != null && (Boolean) r;
    }

    /**
     * 判断用户是否已退出登录
     * 
     * @param request
     * @return
     */
    public static boolean isUserLogout(HttpServletRequest request) {
        return request != null ? isUserLogout(request.getSession()) : false;
    }

    /**
     * 判断用户是否已退出登录
     * 
     * @return
     */
    public static boolean isUserLogout() {
        HttpServletRequest request = getServletRequest();
        return request != null ? isUserLogout(request) : false;
    }

    /**
     * 从Session中移除当前登录用户
     */
    public static void removeCurrentUser() {
        HttpServletRequest request = getServletRequest();
        if (request != null) {
            removeCurrentUser(request);
        }
    }

    /**
     * 从Session中移除登录用户
     * 
     * @param session
     */
    public static void removeCurrentUser(HttpSession session) {
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
            session.setAttribute(USER_LOGOUT_SESSION_KEY, true);
        }
    }

    /**
     * 从Session中移除登录用户
     * 
     * @param request
     */
    public static void removeCurrentUser(HttpServletRequest request) {
        if (request != null) {
            removeCurrentUser(request.getSession());
        }
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
        return request != null ? getCookieUser(request, USER_COOKIE_PATH)
                        : null;
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
        if (request == null) {
            return null;
        }
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
     * 在Session中缓存准备重置密码的用户信息
     * 
     * @param session
     * @param user
     */
    public static void setResetPasswordUser(HttpSession session, User user) {
        if (session != null) {
            session.setAttribute(USER_RESET_PASSWORD_SESSION_KEY, user);
        }
    }

    /**
     * 在Session中缓存准备重置密码的用户信息
     * 
     * @param request
     * @param user
     */
    public static void setResetPasswordUser(HttpServletRequest request,
                    User user) {
        if (request != null) {
            setResetPasswordUser(request.getSession(), user);
        }
    }

    /**
     * 在ServletContext中保存申请重置密码的用户信息，用户将从邮件中得到hash，
     * 后续通过该hash从ServletContext中查找到申请重置密码的用户信息，
     * 同一个Session有效期内，只能申请一次重置密码，以最新的一次为准，之前的都将自动失效
     * 
     * @param session
     * @param hash
     *            发往用户邮箱中得hash值
     * @param user
     *            申请重置密码的用户信息
     */
    public static void setResetPasswordUser(HttpSession session, String hash,
                    User user) {
        if (session != null) {
            cleanResetPasswordHash(session);
            session.setAttribute(USER_RESET_PASSWORD_HASH_SESSION_KEY, hash);
            session.getServletContext().setAttribute(hash, user);
        }
    }

    /**
     * 在ServletContext中保存申请重置密码的用户信息，用户将从邮件中得到hash，
     * 后续通过该hash从ServletContext中查找到申请重置密码的用户信息，
     * 同一个Session有效期内，只能申请一次重置密码，以最新的一次为准，之前的都将自动失效
     * 
     * @param request
     * @param hash
     *            发往用户邮箱中得hash值
     * @param user
     *            申请重置密码的用户信息
     */
    public static void setResetPasswordUser(HttpServletRequest request,
                    String hash, User user) {
        if (request != null) {
            setResetPasswordUser(request.getSession(), hash, user);
        }
    }

    /**
     * 获取准备重置密码的用户信息
     * 
     * @param request
     * @return
     */
    public static User getResetPasswordUser(HttpServletRequest request) {
        return request != null ? (User) SessionUtils.getSessionAttribute(
                        request, USER_RESET_PASSWORD_SESSION_KEY, true) : null;
    }

    /**
     * 从ServletContext或Session中获取申请重置密码的用户信息，从ServletContext中获取后，需要将其放置在Session中
     * 
     * @param request
     * @param hash
     *            发往用户邮箱中得hash值
     * @return
     */
    public static User getResetPasswordUser(HttpServletRequest request,
                    String hash) {
        if (request == null) {
            return null;
        }
        Object obj = ServletContextUtils.getAttribute(request, hash, true);
        User user = null;
        if (obj != null && obj instanceof User) {
            user = (User) obj;
        } else {
            user = getResetPasswordUser(request);
        }
        if (user != null) {
            setResetPasswordUser(request, user);
        }
        return user;
    }

    /**
     * 清除ServletContext中得用户重置密码hash（同一个用户同时只能申请一次重置密码，使用最新的一次）
     * 
     * @param session
     * @return
     */
    public static void cleanResetPasswordHash(HttpSession session) {
        if (session != null) {
            Object hash = session
                            .getAttribute(USER_RESET_PASSWORD_HASH_SESSION_KEY);
            if (hash != null) {
                session.getServletContext().removeAttribute((String) hash);
            }
        }
    }

    /**
     * 清除ServletContext中得用户重置密码hash（同一个用户同时只能申请一次重置密码，使用最新的一次）
     * 
     * @param request
     * @return
     */
    public static void cleanResetPasswordHash(HttpServletRequest request) {
        if (request != null) {
            cleanResetPasswordHash(request.getSession());
        }
    }

    /**
     * 设置对象的所有者为当前登录用户
     * 
     * @param obj
     */
    public static void setOwner(Base obj) {
        HttpServletRequest request = getServletRequest();
        if (request != null) {
            setOwner(request, obj);
        }
    }

    /**
     * 设置对象的所有者
     * 
     * @param obj
     */
    public static void setOwner(HttpServletRequest request, Base obj) {
        if (request != null) {
            User user = getCurrentUser(request);
            if (user != null) {
                obj.setOwner(user.getId());
                obj.setModifier(user.getId());
            }
        }
    }

    /**
     * 设置对象的修改者为当前登录用户
     * 
     * @param obj
     */
    public static void setModifier(Base obj) {
        HttpServletRequest request = getServletRequest();
        if (request != null) {
            setModifier(request, obj);
        }
    }

    /**
     * 设置对象的修改者
     * 
     * @param obj
     */
    public static void setModifier(HttpServletRequest request, Base obj) {
        if (request != null) {
            User user = getCurrentUser(request);
            if (user != null) {
                obj.setModifier(user.getId());
            }
        }
    }

}
