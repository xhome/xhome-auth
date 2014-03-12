package org.xhome.xauth.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 10, 201311:28:58 PM
 * @description
 */
public class TestAuthLogManageListener implements AuthLogManageListener {

	private Logger logger = LoggerFactory
			.getLogger(TestAuthLogManageListener.class);

	@Override
	public boolean beforeAuthLogManage(User oper, short action,
			AuthLog authLog, Object... args) {
		logger.debug("TEST BEFORE AUTHLOG MANAGE LISTENER {} {} ",
				oper.getName(), action);
		return true;
	}

	@Override
	public void afterAuthLogManage(User oper, short action, short result,
			AuthLog authLog, Object... args) {
		logger.debug("TEST AFTER AUTHLOG MANAGE LISTENER {} {}",
				oper.getName(), action);
	}

}
