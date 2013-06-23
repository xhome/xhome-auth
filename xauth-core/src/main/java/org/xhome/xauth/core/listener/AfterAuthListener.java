package org.xhome.xauth.core.listener;

import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 10:08:22 PM
 * @description 用户认证结果监听器接口
 */
public interface AfterAuthListener {

	/**
	 * 用户认证结果监听接口
	 * 
	 * @param result
	 *            认证结果
	 * @param user
	 *            用户认证结果信息
	 */
	public void afterAuth(short result, User user);

}
