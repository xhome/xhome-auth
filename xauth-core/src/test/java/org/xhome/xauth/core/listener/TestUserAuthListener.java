package org.xhome.xauth.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20134:43:57 PM
 * @description
 */
public class TestUserAuthListener implements UserAuthListener {

	private Logger logger = LoggerFactory.getLogger(TestUserAuthListener.class);

	@Override
	public boolean beforeUserAuth(User user) {
		logger.debug("TEST BEFORE USER AUTH LISTENER {}", user.getName());
		return true;
	}

	@Override
	public void afterUserAuth(User before, User after, short result) {
		logger.debug("TEST AFTER USER AUTH LISTENER {} {}", before.getName(),
				result);
	}

}
