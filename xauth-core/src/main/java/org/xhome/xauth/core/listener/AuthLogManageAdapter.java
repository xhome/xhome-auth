package org.xhome.xauth.core.listener;

import org.xhome.xauth.AuthLog;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 10, 20139:10:12 PM
 * @description 
 */
public class AuthLogManageAdapter implements AuthLogManageListener {

	public boolean beforeAuthLogManage(User oper, short action, AuthLog authLog, Object ...args) {
		return true;
	}
	
	public void afterAuthLogManage(User oper, short action, short result, AuthLog authLog, Object ...args) {}
	
}
