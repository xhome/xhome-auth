package org.xhome.xauth.core.listener;

import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 8, 20139:50:03 PM
 * @description 角色添加结果监听器接口
 */
public interface AfterAddRoleListener {

	/**
	 * 角色添加结果监听器接口
	 * 
	 * @param role 已添加的角色信息
	 * @param user 执行该操作的用户
	 */
	public void afterAddRole(Role role, User user);
	
}
