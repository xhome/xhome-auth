package org.xhome.xauth.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.Config;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Nov 14, 20131:02:12 AM
 * @describe 
 */
public class TestConfigManageListener implements ConfigManageListener {
	private Logger logger = LoggerFactory.getLogger(TestConfigManageListener.class);
	
	@Override
	public boolean beforeConfigManage(User oper, short action, Config config,
			Object... args) {
		logger.debug("TEST BEFORE CONFIG MANAGE LISTENER {} {} {}", oper.getName(), action, config != null ? config.getItem() : "NULL");
		return true;
	}

	@Override
	public void afterConfigManage(User oper, short action, short result,
			Config config, Object... args) {
		logger.debug("TEST AFTER CONFIG MANAGE LISTENER {} {} {}", oper.getName(), action, config != null ? config.getItem() : "NULL");
	}

}
