package org.xhome.xauth.core.auth;

import org.xhome.xauth.AuthException;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20132:49:04 PM
 * @description 
 */
public interface UserAuth {

	/**
	 * 用户认证
	 * @param user 待认证的用户
	 * @return 返回认证后的用户信息
	 */
	public User auth(User user) throws AuthException;
	
}
