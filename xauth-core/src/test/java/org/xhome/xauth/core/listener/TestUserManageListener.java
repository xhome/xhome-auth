package org.xhome.xauth.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 10, 201311:28:58 PM
 * @description
 */
public class TestUserManageListener implements UserManageListener {

	private Logger logger = LoggerFactory
			.getLogger(TestUserManageListener.class);

	@Override
	public boolean beforeUserManage(User oper, short action, User user,
			Object... args) {
		logger.debug("TEST BEFORE USER MANAGE LISTENER {} {} {}",
				oper.getName(), action, user != null ? user.getName() : "NULL");
		return true;
	}

	@Override
	public void afterUserManage(User oper, short action, short result,
			User user, Object... args) {
		logger.debug("TEST AFTER USER MANAGE LISTENER {} {} {}",
				oper.getName(), action, user != null ? user.getName() : "NULL");
	}

}
