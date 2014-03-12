package org.xhome.xauth.core.service;

import org.xhome.xauth.AuthException;
import org.xhome.xauth.User;
import org.xhome.xauth.core.auth.UserAuth;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20134:13:59 PM
 * @description
 */
public class TestUserAuth implements UserAuth {

	@Override
	public User auth(User user) throws AuthException {
		// throw new AuthException(Status.ERROR, "TEST USER AUTH");
		return user;
	}

}
