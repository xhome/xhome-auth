package org.xhome.xauth.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.xhome.xauth.web.util.AuthUtils;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Mar 17, 2014
 * @describe 认证类Session监听器，Session被注销后进行必要的清理工作，避免内存泄露
 */
public class AuthCleanupListener implements HttpSessionListener {

    /**
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {}

    /**
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 如果用户申请过重置密码，会在ServletContext中存放申请重置密码的用户信息，
        // 用户Session注销后，需要将其从ServletContext中移除
        HttpSession session = se.getSession();
        Object hash = session
                        .getAttribute(AuthUtils.USER_RESET_PASSWORD_HASH_SESSION_KEY);
        if (hash != null) {
            session.getServletContext().removeAttribute((String) hash);
        }
    }

}
