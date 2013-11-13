package org.xhome.xauth.core.listener;

import org.xhome.xauth.User;
import org.xhome.xauth.Config;

/**
 * @project xauth-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Nov 14, 201312:22:03 AM
 * @describe 配置项管理监听器接口
 */
public interface ConfigManageListener {
	
	/**
	 * 配置项管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param config 待管理的配置项信息
	 * @param args 除config之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeConfigManage(User oper, short action, Config config, Object ...args);
	
	/**
	 * 配置项管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param config 待管理的配置项信息
	 * @param args 除config之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterConfigManage(User oper, short action, short result, Config config, Object ...args);

}
