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
public class RoleManageAdapter implements RoleManageListener {

	public boolean beforeRoleManage(User oper, short action, Role role, Object ...args) {
		return true;
	}
	
	public void afterRoleManage(User oper, short action, short result, Role role, Object ...args) {}
	
}
