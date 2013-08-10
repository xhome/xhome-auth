package org.xhome.xauth.core.listener;

import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 10, 20139:10:12 PM
 * @description 
 */
public class UserManageAdapter implements UserManageListener {

	public boolean beforeUserManage(User oper, short action, User user, Object ...args) {
		return true;
	}
	
	public void afterUserManage(User oper, short action, short result, User user, Object ...args) {}
	
}
