package org.xhome.xauth.core.listener;

import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 10:08:22 PM
 * @description 用户认证监听器接口
 */
public interface BeforeAuthListener {

	/**
	 * 用户认证监听接口
	 * 
	 * @param user
	 *            用户认证信息
	 * @return 是否允许执行用户认证操作：true/false（允许/不允许）
	 */
	public boolean beforeAuth(User user);

}
