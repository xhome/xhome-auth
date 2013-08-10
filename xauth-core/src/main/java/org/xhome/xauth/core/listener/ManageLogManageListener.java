package org.xhome.xauth.core.listener;

import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 8, 20139:46:12 PM
 * @description 管理日志管理监听器接口
 */
public interface ManageLogManageListener {
	
	/**
	 * 管理日志管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param manageLog 待管理的管理日志信息
	 * @param args 除manageLog之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeManageLogManage(User oper, short action, ManageLog manageLog, Object ...args);
	
	/**
	 * 管理日志管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param manageLog 待管理的管理日志信息
	 * @param args 除manageLog之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterManageLogManage(User oper, short action, short result, ManageLog manageLog, Object ...args);

}
