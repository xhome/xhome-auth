package org.xhome.xauth.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;

/**
 * @project xmanage-core
 * @manageor jhat
 * @email cpf624@126.com
 * @date Aug 10, 201311:28:58 PM
 * @description
 */
public class TestManageLogManageListener implements ManageLogManageListener {

	private Logger logger = LoggerFactory
			.getLogger(TestManageLogManageListener.class);

	@Override
	public boolean beforeManageLogManage(User oper, short action,
			ManageLog manageLog, Object... args) {
		logger.debug("TEST BEFORE MANAGELOG MANAGE LISTENER {} {} ",
				oper.getName(), action);
		return true;
	}

	@Override
	public void afterManageLogManage(User oper, short action, short result,
			ManageLog manageLog, Object... args) {
		logger.debug("TEST AFTER MANAGELOG MANAGE LISTENER {} {}",
				oper.getName(), action);
	}

}
