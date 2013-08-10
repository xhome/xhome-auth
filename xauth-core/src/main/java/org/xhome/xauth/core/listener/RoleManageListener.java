package org.xhome.xauth.core.listener;

import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 8, 20139:46:12 PM
 * @description 角色管理监听器接口
 */
public interface RoleManageListener {
	
	/**
	 * 角色管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param role 待管理的角色信息
	 * @param args 除role之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeRoleManage(User oper, short action, Role role, Object ...args);
	
	/**
	 * 角色管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param role 待管理的角色信息
	 * @param args 除role之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterRoleManage(User oper, short action, short result, Role role, Object ...args);

}
