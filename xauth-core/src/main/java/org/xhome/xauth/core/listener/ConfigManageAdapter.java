package org.xhome.xauth.core.listener;

import org.xhome.xauth.User;
import org.xhome.xauth.Config;

/**
 * @project xauth-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Nov 14, 201312:21:55 AM
 * @describe 
 */
public class ConfigManageAdapter implements ConfigManageListener {

	public boolean beforeConfigManage(User oper, short action, Config config, Object ...args) {
		return true;
	}
	
	public void afterConfigManage(User oper, short action, short result, Config config, Object ...args) {}
	
}
