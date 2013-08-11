package org.xhome.xauth.core.listener;

import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20134:21:03 PM
 * @description 
 */
public class UserAuthAdapter implements UserAuthListener {

	@Override
	public boolean beforeUserAuth(User user) {
		return true;
	}

	@Override
	public void afterUserAuth(User before, User after, short result) {}

}
