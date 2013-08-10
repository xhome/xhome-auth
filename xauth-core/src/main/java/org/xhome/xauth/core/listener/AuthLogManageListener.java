package org.xhome.xauth.core.listener;

import org.xhome.xauth.AuthLog;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 8, 20139:46:12 PM
 * @description 用户认证日志管理监听器接口
 */
public interface AuthLogManageListener {
	
	/**
	 * 用户认证日志管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param authLog 待管理的用户认证日志信息
	 * @param args 除authLog之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeAuthLogManage(User oper, short action, AuthLog authLog, Object ...args);
	
	/**
	 * 用户认证日志管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param authLog 待管理的用户认证日志信息
	 * @param args 除authLog之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterAuthLogManage(User oper, short action, short result, AuthLog authLog, Object ...args);

}
