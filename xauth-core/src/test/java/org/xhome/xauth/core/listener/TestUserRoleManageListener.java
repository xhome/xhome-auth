package org.xhome.xauth.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 10, 201311:28:58 PM
 * @description
 */
public class TestUserRoleManageListener implements UserRoleManageListener {

	private Logger logger = LoggerFactory
			.getLogger(TestUserRoleManageListener.class);

	@Override
	public boolean beforeUserRoleManage(User oper, short action, User user,
			Role role, Object... args) {
		logger.debug("TEST BEFORE USER ROLE MANAGE LISTENER {} {} {}",
				oper.getName(), action, user != null ? user.getName() : "NULL");
		return true;
	}

	@Override
	public void afterUserRoleManage(User oper, short action, short result,
			User user, Role role, Object... args) {
		logger.debug("TEST AFTER USER ROLE MANAGE LISTENER {} {} {}",
				oper.getName(), action, user != null ? user.getName() : "NULL");
	}

}
