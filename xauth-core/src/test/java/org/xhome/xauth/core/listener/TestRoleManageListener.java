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
public class TestRoleManageListener implements RoleManageListener {

	private Logger logger = LoggerFactory
			.getLogger(TestRoleManageListener.class);

	@Override
	public boolean beforeRoleManage(User oper, short action, Role role,
			Object... args) {
		logger.debug("TEST BEFORE ROLE MANAGE LISTENER {} {} {}",
				oper.getName(), action, role != null ? role.getName() : "NULL");
		return true;
	}

	@Override
	public void afterRoleManage(User oper, short action, short result,
			Role role, Object... args) {
		logger.debug("TEST AFTER ROLE MANAGE LISTENER {} {} {}",
				oper.getName(), action, role != null ? role.getName() : "NULL");
	}

}
