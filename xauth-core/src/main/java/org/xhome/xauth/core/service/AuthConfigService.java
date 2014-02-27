package org.xhome.xauth.core.service;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Jan 28, 2014
 * @describe 认证管理参数
 */
public interface AuthConfigService extends ConfigService {

	String ITEM_NEXT_PAGE = "xauth_next_page"; // 认证跳转地址

	String ITEM_TRY_LIMIT = "xauth_try_limit"; // 认证尝试次数

	String ITEM_LOCK_TIME = "xauth_lock_time"; // 认证锁定时间

	String ITEM_ALLOW_AUTH_LOG = "xauth_allow_auth_log"; // 认证日志

	String ITEM_ALLOW_MANAGE_LOG = "xauth_allow_manage_log"; // 管理日志

	/**
	 * 获取认证成功后的跳转地址
	 * 
	 * @return
	 */
	public String getNextPage();

	/**
	 * 获取认证尝试次数
	 * 
	 * @return
	 */
	public int getAuthTryLimit();

	/**
	 * 获取认证锁定时间
	 * 
	 * @return
	 */
	public long getAuthLockTime();

	/**
	 * 判断是否开启认证日志
	 * 
	 * @return
	 */
	public boolean allowAuthLog();

	/**
	 * 判断是否开启管理日志
	 * 
	 * @return
	 */
	public boolean allowManageLog();

}
