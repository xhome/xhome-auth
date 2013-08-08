package org.xhome.xauth.core.listener;

import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 8, 20139:46:12 PM
 * @description 添加角色前监听器接口
 */
public interface BeforeAddRoleListener {
	
	/**
	 * 添加角色前监听器接口
	 * 
	 * @param role 待添加的角色信息
	 * @param user 执行该操作的用户
	 * @return 是否允许执行角色添加操作：true/false（允许/不允许）
	 */
	public boolean beforeAddRole(Role role, User user);

}
