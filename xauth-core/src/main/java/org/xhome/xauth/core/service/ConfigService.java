package org.xhome.xauth.core.service;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Config;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Nov 14, 201312:22:30 AM
 * @describe
 */
public interface ConfigService {

	String ITEM_BASE_URL = "base_url";

	// public int addConfig(User oper, Config config);

	/**
	 * 更新配置项
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param config
	 *            待更新的配置项
	 * @return
	 */
	public int updateConfig(User oper, Config config);

	// public int lockConfig(User oper, Config config);

	// public int unlockConfig(User oper, Config config);

	// public int removeConfig(User oper, Config config);

	// public int removeConfigs(User oper, List<Config> configs);

	// public int deleteConfig(User oper, Config config);

	// public int deleteConfigs(User oper, List<Config> configs);

	/**
	 * 判断配置项是否存在
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param config
	 *            待判断的配置项
	 * @return
	 */
	public boolean isConfigExists(User oper, Config config);

	public boolean isConfigUpdateable(User oper, Config config);

	// public boolean isConfigLocked(User oper, Config config);

	// public boolean isConfigRemoveable(User oper, Config config);

	// public boolean isConfigDeleteable(User oper, Config config);

	/**
	 * 获取指定ID的配置项
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param id
	 *            配置项ID
	 * @return
	 */
	public Config getConfig(User oper, long id);

	/**
	 * 获取指定的配置项
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param item
	 *            配置项
	 * @return
	 */
	public Config getConfig(User oper, String item);

	/**
	 * 查询配置项
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param query
	 *            查询条件
	 * @return
	 */
	public List<Config> getConfigs(User oper, QueryBase query);

	/**
	 * 统计配置项
	 * 
	 * @param oper
	 *            当前操作用户
	 * @param query
	 *            统计条件
	 * @return
	 */
	public long countConfigs(User oper, QueryBase query);

	/**
	 * 获取系统基地址
	 * 
	 * @return
	 */
	public String getBaseURL();

}
