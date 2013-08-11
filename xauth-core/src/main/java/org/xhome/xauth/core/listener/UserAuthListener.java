package org.xhome.xauth.core.listener;

import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 11, 20134:19:01 PM
 * @description 用户认证监听器
 */
public interface UserAuthListener {
	
	/**
	 * 用户认证前监听
	 * @param user 待认证的用户信息
	 * @return 是否允许认证：true/false（允许/不允许）
	 */
	public boolean beforeUserAuth(User user);
	
	/**
	 * 用户认证后监听
	 * @param before 认证前的用户信息
	 * @param after 认证后的用户信息，如果与before相同，说明找到用于认证的用户信息
	 * @param result 认证结果
	 */
	public void afterUserAuth(User before, User after, short result);

}
