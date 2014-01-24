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

	// public int addConfig(User oper, Config config);

	public int updateConfig(User oper, Config config);

	// public int lockConfig(User oper, Config config);

	// public int unlockConfig(User oper, Config config);

	// public int removeConfig(User oper, Config config);

	// public int removeConfigs(User oper, List<Config> configs);

	// public int deleteConfig(User oper, Config config);

	// public int deleteConfigs(User oper, List<Config> configs);

	public boolean isConfigExists(User oper, Config config);

	// public boolean isConfigUpdateable(User oper, Config config);

	// public boolean isConfigLocked(User oper, Config config);

	// public boolean isConfigRemoveable(User oper, Config config);

	// public boolean isConfigDeleteable(User oper, Config config);

	public Config getConfig(User oper, long id);

	public Config getConfig(User oper, String item);

	public List<Config> getConfigs(User oper);

	public List<Config> getConfigs(User oper, QueryBase query);

	public long countConfigs(User oper);

	public long countConfigs(User oper, QueryBase query);

}
