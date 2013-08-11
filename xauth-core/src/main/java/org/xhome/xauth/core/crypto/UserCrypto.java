package org.xhome.xauth.core.crypto;

import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20133:13:08 PM
 * @description 
 */
public interface UserCrypto {
	
	/**
	 * 加密用户信息
	 * @param user 待加密的用户
	 */
	public void crypto(User user);

}
