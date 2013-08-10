package org.xhome.xauth.core.listener;

import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 10, 20139:10:12 PM
 * @description 
 */
public class UserRoleManageAdapter implements UserRoleManageListener {

	public boolean beforeUserRoleManage(User oper, short action, User user, Role role, Object ...args) {
		return true;
	}
	
	public void afterUserRoleManage(User oper, short action, short result, User user, Role role, Object ...args) {}
	
}
