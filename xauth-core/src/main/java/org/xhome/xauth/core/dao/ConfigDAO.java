package org.xhome.xauth.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Config;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Nov 14, 201312:21:24 AM
 * @describe
 */
public interface ConfigDAO {

	/**
	 * 添加配置项
	 * 
	 * @param config
	 *            待添加的配置项
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	// public int addConfig(Config config);

	/**
	 * 更新配置项
	 * 
	 * @param config
	 *            待更新的配置项
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	public int updateConfig(Config config);

	/**
	 * 锁定配置项
	 * 
	 * @param config
	 *            待锁定的配置项
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	// public int lockConfig(Config config);

	/**
	 * 解除配置项锁定
	 * 
	 * @param config
	 *            待解除锁定的配置项
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	// public int unlockConfig(Config config);

	/**
	 * 移除（标记删除）配置项
	 * 
	 * @param config
	 *            待移除的配置项
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	// public int removeConfig(Config config);

	/**
	 * 删除（物理删除）配置项
	 * 
	 * @param config
	 *            待删除的配置项
	 * @return 返回添加结果状态码，1表示操作成功
	 */
	// public int deleteConfig(Config config);

	/**
	 * 判断配置项是否存在
	 * 
	 * @param config
	 *            待判断的配置项
	 * @return
	 */
	public boolean isConfigExists(Config config);

	/**
	 * 判断配置项是否可以更新
	 * 
	 * @param config
	 *            待判断的配置项
	 * @return
	 */
	// public boolean isConfigUpdateable(Config config);

	/**
	 * 判断配置项是否被锁定
	 * 
	 * @param config
	 *            待判断的配置项
	 * @return
	 */
	// public boolean isConfigLocked(Config config);

	/**
	 * 判断配置项是否可被移除（标记删除）
	 * 
	 * @param config
	 *            待判断的配置项
	 * @return
	 */
	// public boolean isConfigRemoveable(Config config);

	/**
	 * 判断配置项是否可被删除（物理删除）
	 * 
	 * @param config
	 *            待判断的配置项
	 * @return
	 */
	// public boolean isConfigDeleteable(Config config);

	/**
	 * 按ID查询配置项
	 * 
	 * @param id
	 *            待查询的配置项ID
	 * @return
	 */
	public Config queryConfigById(Long id);

	/**
	 * 按名称查询配置项
	 * 
	 * @param item
	 *            待查询的配置项名称
	 * @return
	 */
	public Config queryConfigByItem(String item);

	/**
	 * 条件查询配置项
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	public List<Config> queryConfigs(QueryBase query);

	/**
	 * 条件统计配置项
	 * 
	 * @param query
	 *            统计条件
	 * @return
	 */
	public long countConfigs(QueryBase query);

}
