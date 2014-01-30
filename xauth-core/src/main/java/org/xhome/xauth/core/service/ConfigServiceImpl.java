package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Config;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.dao.ConfigDAO;
import org.xhome.xauth.core.listener.ConfigManageListener;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Nov 14, 201312:22:36 AM
 * @describe
 */
@Service
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	protected ConfigDAO configDAO;
	@Autowired
	protected ManageLogService manageLogService;

	@Autowired(required = false)
	protected List<ConfigManageListener> configManageListeners;

	protected Logger logger;

	public ConfigServiceImpl() {
		logger = LoggerFactory.getLogger(ConfigService.class);
	}

	// @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor =
	// Throwable.class)
	// @Override
	// public int addConfig(User oper, Config config) {
	// String item = config.getItem();
	//
	// if (!this.beforeConfigManage(oper, Action.ADD, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("try to add config {}, but it's blocked", item);
	// }
	//
	// this.logManage(item, Action.ADD, null, Status.BLOCKED, oper);
	// this.afterConfigManage(oper, Action.ADD, Status.BLOCKED, config);
	// return Status.BLOCKED;
	// }
	//
	// if (configDAO.isConfigExists(config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("try to add config {}, but it's already exists",
	// item);
	// }
	//
	// this.logManage(item, Action.ADD, null, Status.EXISTS, oper);
	// this.afterConfigManage(oper, Action.ADD, Status.EXISTS, config);
	// return Status.EXISTS;
	// }
	//
	// config.setStatus(Status.OK);
	// config.setVersion((short) 0);
	// Timestamp t = new Timestamp(System.currentTimeMillis());
	// config.setCreated(t);
	// config.setModified(t);
	//
	// short r = configDAO.addConfig(config) == 1 ? Status.SUCCESS
	// : Status.ERROR;
	//
	// if (logger.isDebugEnabled()) {
	// if (r == Status.SUCCESS) {
	// logger.debug("success to add config {}", item);
	// } else {
	// logger.debug("fail to add config {}", item);
	// }
	// }
	//
	// this.logManage(item, Action.ADD, config.getId(), r, oper);
	// this.afterConfigManage(oper, Action.ADD, r, config);
	// return r;
	// }

	/**
	 * @see org.xhome.xauth.core.service.ConfigService#updateConfig(org.xhome.xauth
	 *      .User, org.xhome.xauth.Config)
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateConfig(User oper, Config config) {
		String item = config.getItem();
		Long id = config.getId();

		if (!this.beforeConfigManage(oper, Action.UPDATE, config)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update config {}[{}], but it's blocked",
						item, id);
			}

			this.logManage(item, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterConfigManage(oper, Action.UPDATE, Status.BLOCKED, config);
			return Status.BLOCKED;
		}

		Config old = configDAO.queryConfigById(id);

		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update config {}[{}], but it's not exists",
						item, id);
			}

			this.logManage(item, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterConfigManage(oper, Action.UPDATE, Status.NOT_EXISTS,
					config);
			return Status.NOT_EXISTS;
		}

		String oldName = old.getItem();

		if (!old.getVersion().equals(config.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update config {}[{}], but version not match",
						oldName, id);
			}

			this.logManage(oldName, Action.UPDATE, id,
					Status.VERSION_NOT_MATCH, oper);
			this.afterConfigManage(oper, Action.UPDATE,
					Status.VERSION_NOT_MATCH, config);
			return Status.VERSION_NOT_MATCH;
		}

		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update config {}[{}]",
						oldName, id);
			}

			this.logManage(oldName, Action.UPDATE, id, status, oper);
			this.afterConfigManage(oper, Action.UPDATE, Status.EXISTS, config);
			return status;
		}

		if (!oldName.equals(item) && configDAO.isConfigExists(config)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update config {}[{}] to {}, but it's exists",
						oldName, id, item);
			}
			this.logManage(oldName, Action.UPDATE, id, Status.EXISTS, oper);
			this.afterConfigManage(oper, Action.UPDATE, Status.EXISTS, config);
			return Status.EXISTS;
		}

		config.setOwner(old.getOwner());
		config.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		config.setModified(t);

		short r = configDAO.updateConfig(config) == 1 ? Status.SUCCESS
				: Status.ERROR;
		if (r == Status.SUCCESS) {
			config.incrementVersion();
		}

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update config {}[{}]", oldName, id);
			} else {
				logger.debug("fail to update config {}[{}]", oldName, id);
			}
		}

		this.logManage(oldName, Action.UPDATE, id, r, oper);
		this.afterConfigManage(oper, Action.UPDATE, r, config);
		return r;
	}

	// @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor =
	// Throwable.class)
	// @Override
	// public int lockConfig(User oper, Config config) {
	// String item = config.getItem();
	// Long id = config.getId();
	//
	// if (!this.beforeConfigManage(oper, Action.LOCK, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("try to lock config {}[{}], but it's blocked",
	// item, id);
	// }
	//
	// this.logManage(item, Action.LOCK, null, Status.BLOCKED, oper);
	// this.afterConfigManage(oper, Action.LOCK, Status.BLOCKED, config);
	// return Status.BLOCKED;
	// }
	//
	// short r = configDAO.lockConfig(config) == 1 ? Status.SUCCESS
	// : Status.ERROR;
	//
	// if (logger.isDebugEnabled()) {
	// if (r == Status.SUCCESS) {
	// logger.debug("success to lock config {}[{}]", item, id);
	// } else {
	// logger.debug("fail to lock config {}[{}]", item, id);
	// }
	// }
	//
	// this.logManage(item, Action.LOCK, id, r, oper);
	// this.afterConfigManage(oper, Action.LOCK, r, config);
	// return r;
	// }

	// @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor =
	// Throwable.class)
	// @Override
	// public int unlockConfig(User oper, Config config) {
	// String item = config.getItem();
	// Long id = config.getId();
	//
	// if (!this.beforeConfigManage(oper, Action.UNLOCK, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("try to unlock config {}[{}], but it's blocked",
	// item, id);
	// }
	//
	// this.logManage(item, Action.UNLOCK, null, Status.BLOCKED, oper);
	// this.afterConfigManage(oper, Action.UNLOCK, Status.BLOCKED, config);
	// return Status.BLOCKED;
	// }
	//
	// short r = configDAO.unlockConfig(config) == 1 ? Status.SUCCESS
	// : Status.ERROR;
	//
	// if (logger.isDebugEnabled()) {
	// if (r == Status.SUCCESS) {
	// logger.debug("success to unlock config {}[{}]", item, id);
	// } else {
	// logger.debug("fail to unlock config {}[{}]", item, id);
	// }
	// }
	//
	// this.logManage(item, Action.UNLOCK, id, r, oper);
	// this.afterConfigManage(oper, Action.UNLOCK, r, config);
	// return r;
	// }

	// @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor =
	// Throwable.class)
	// @Override
	// public int removeConfig(User oper, Config config) {
	// String item = config.getItem();
	// Long id = config.getId();
	//
	// if (!this.beforeConfigManage(oper, Action.REMOVE, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("try to remove config {}[{}], but it's blocked",
	// item, id);
	// }
	//
	// this.logManage(item, Action.REMOVE, null, Status.BLOCKED, oper);
	// this.afterConfigManage(oper, Action.REMOVE, Status.BLOCKED, config);
	// return Status.BLOCKED;
	// }
	//
	// short r = Status.SUCCESS;
	// if (configDAO.isConfigRemoveable(config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("remove config {}[{}]", item, id);
	// }
	// configDAO.removeConfig(config);
	// } else {
	// if (logger.isDebugEnabled()) {
	// logger.debug("config {}[{}] isn't removeable", item, id);
	// }
	// r = Status.NO_REMOVE;
	// }
	//
	// this.logManage(item, Action.REMOVE, id, r, oper);
	// this.afterConfigManage(oper, Action.REMOVE, r, config);
	// return r;
	// }

	// @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor =
	// Throwable.class)
	// @Override
	// public int removeConfigs(User oper, List<Config> configs) {
	// int r = Status.SUCCESS;
	// for (Config config : configs) {
	// r = this.removeConfig(oper, config);
	// if (r != Status.SUCCESS) {
	// throw new RuntimeException("fail to remove config ["
	// + config.getId() + "]" + config.getItem());
	// }
	// }
	// return r;
	// }

	// @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor =
	// Throwable.class)
	// @Override
	// public int deleteConfig(User oper, Config config) {
	// String item = config.getItem();
	// Long id = config.getId();
	//
	// if (!this.beforeConfigManage(oper, Action.DELETE, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("try to delete config {}[{}], but it's blocked",
	// item, id);
	// }
	//
	// this.logManage(item, Action.DELETE, null, Status.BLOCKED, oper);
	// this.afterConfigManage(oper, Action.DELETE, Status.BLOCKED, config);
	// return Status.BLOCKED;
	// }
	//
	// short r = Status.SUCCESS;
	// if (configDAO.isConfigDeleteable(config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("delete config {}[{}]", item, id);
	// }
	// configDAO.deleteConfig(config);
	// } else {
	// if (logger.isDebugEnabled()) {
	// logger.debug("config {}[{}] isn't deleteable", item, id);
	// }
	// r = Status.NO_DELETE;
	// }
	//
	// this.logManage(item, Action.DELETE, id, r, oper);
	// this.afterConfigManage(oper, Action.DELETE, r, config);
	// return r;
	// }

	// @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor =
	// Throwable.class)
	// @Override
	// public int deleteConfigs(User oper, List<Config> configs) {
	// int r = Status.SUCCESS;
	// for (Config config : configs) {
	// r = this.deleteConfig(oper, config);
	// if (r != Status.SUCCESS) {
	// throw new RuntimeException("fail to delete config ["
	// + config.getId() + "]" + config.getItem());
	// }
	// }
	// return r;
	// }

	/**
	 * @see org.xhome.xauth.core.service.ConfigService#isConfigExists(org.xhome.xauth
	 *      .User, org.xhome.xauth.Config)
	 */
	@Override
	public boolean isConfigExists(User oper, Config config) {
		String item = config.getItem();

		if (!this.beforeConfigManage(oper, Action.IS_EXISTS, config)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge exists of config {}, but it's blocked",
						item);
			}

			this.logManage(item, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterConfigManage(oper, Action.IS_EXISTS, Status.BLOCKED,
					config);
			return false;
		}

		boolean e = configDAO.isConfigExists(config);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of config {}", item);
			} else {
				logger.debug("not exists of config {}", item);
			}
		}

		this.logManage(item, Action.IS_EXISTS, config.getId(), Status.SUCCESS,
				oper);
		this.afterConfigManage(oper, Action.IS_EXISTS, Status.SUCCESS, config);
		return e;
	}

	@Override
	public boolean isConfigUpdateable(User oper, Config config) {
		String item = config.getItem();
		Long id = config.getId();

		if (!this.beforeConfigManage(oper, Action.IS_UPDATEABLE, config)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge updateable of config {}[{}], but it's blocked",
						item, id);
			}

			this.logManage(item, Action.IS_UPDATEABLE, null, Status.BLOCKED,
					oper);
			this.afterConfigManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED,
					config);
			return false;
		}

		boolean e = configDAO.isConfigUpdateable(config);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("config {}[{}] is updateable", item, id);
			} else {
				logger.debug("config {}[{}] isn't updateable", item, id);
			}
		}

		this.logManage(item, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterConfigManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS,
				config);
		return e;
	}

	// @Override
	// public boolean isConfigLocked(User oper, Config config) {
	// String item = config.getItem();
	// Long id = config.getId();
	//
	// if (!this.beforeConfigManage(oper, Action.IS_LOCKED, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug(
	// "try to juge locked of config {}[{}], but it's blocked",
	// item, id);
	// }
	//
	// this.logManage(item, Action.IS_LOCKED, null, Status.BLOCKED, oper);
	// this.afterConfigManage(oper, Action.IS_LOCKED, Status.BLOCKED,
	// config);
	// return false;
	// }
	//
	// boolean e = configDAO.isConfigLocked(config);
	//
	// if (logger.isDebugEnabled()) {
	// if (e) {
	// logger.debug("config {}[{}] is locked", item, id);
	// } else {
	// logger.debug("config {}[{}] isn't locked", item, id);
	// }
	// }
	//
	// this.logManage(item, Action.IS_LOCKED, id, Status.SUCCESS, oper);
	// this.afterConfigManage(oper, Action.IS_LOCKED, Status.SUCCESS, config);
	// return e;
	// }

	// @Override
	// public boolean isConfigRemoveable(User oper, Config config) {
	// String item = config.getItem();
	// Long id = config.getId();
	//
	// if (!this.beforeConfigManage(oper, Action.IS_REMOVEABLE, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug(
	// "try to juge removeable of config {}[{}], but it's blocked",
	// item, id);
	// }
	//
	// this.logManage(item, Action.IS_REMOVEABLE, null, Status.BLOCKED,
	// oper);
	// this.afterConfigManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED,
	// config);
	// return false;
	// }
	//
	// boolean e = configDAO.isConfigRemoveable(config);
	//
	// if (logger.isDebugEnabled()) {
	// if (e) {
	// logger.debug("config {}[{}] is removeable", item, id);
	// } else {
	// logger.debug("config {}[{}] isn't removeable", item, id);
	// }
	// }
	//
	// this.logManage(item, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
	// this.afterConfigManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS,
	// config);
	// return e;
	// }

	// @Override
	// public boolean isConfigDeleteable(User oper, Config config) {
	// String item = config.getItem();
	// Long id = config.getId();
	//
	// if (!this.beforeConfigManage(oper, Action.IS_DELETEABLE, config)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug(
	// "try to juge deleteable of config {}[{}], but it's blocked",
	// item, id);
	// }
	//
	// this.logManage(item, Action.IS_DELETEABLE, null, Status.BLOCKED,
	// oper);
	// this.afterConfigManage(oper, Action.IS_DELETEABLE, Status.BLOCKED,
	// config);
	// return false;
	// }
	//
	// boolean e = configDAO.isConfigDeleteable(config);
	//
	// if (logger.isDebugEnabled()) {
	// if (e) {
	// logger.debug("config {}[{}] is deleteable", item, id);
	// } else {
	// logger.debug("config {}[{}] isn't deleteable", item, id);
	// }
	// }
	//
	// this.logManage(item, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
	// this.afterConfigManage(oper, Action.IS_DELETEABLE, Status.SUCCESS,
	// config);
	// return e;
	// }

	/**
	 * @see org.xhome.xauth.core.service.ConfigService#getConfig(org.xhome.xauth
	 *      .User, long)
	 */
	@Override
	public Config getConfig(User oper, long id) {
		if (!this.beforeConfigManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get config of id {}, but it's blocked", id);
			}

			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterConfigManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}

		Config config = configDAO.queryConfigById(id);

		String item = null;
		if (logger.isDebugEnabled()) {
			if (config != null) {
				item = config.getItem();
				logger.debug("get config {}[{}]", item, id);
			} else {
				logger.debug("config of id {} is not exists", id);
			}
		}

		this.logManage(item, Action.GET, id, Status.SUCCESS, oper);
		this.afterConfigManage(oper, Action.GET, Status.SUCCESS, config, id);
		return config;
	}

	/**
	 * @see org.xhome.xauth.core.service.ConfigService#getConfig(org.xhome.xauth
	 *      .User, String)
	 */
	@Override
	public Config getConfig(User oper, String item) {
		if (!this.beforeConfigManage(oper, Action.GET, null, item)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get config {}, but it's blocked", item);
			}

			this.logManage(item, Action.GET, null, Status.BLOCKED, oper);
			this.afterConfigManage(oper, Action.GET, Status.BLOCKED, null, item);
			return null;
		}

		Config config = configDAO.queryConfigByItem(item);

		Long id = null;
		if (logger.isDebugEnabled()) {
			if (config != null) {
				id = config.getId();
				logger.debug("get config {}[{}]", item, id);
			} else {
				logger.debug("config {} is not exists", item);
			}
		}

		this.logManage(item, Action.GET, id, Status.SUCCESS, oper);
		this.afterConfigManage(oper, Action.GET, Status.SUCCESS, config, item);
		return config;
	}

	/**
	 * @see org.xhome.xauth.core.service.ConfigService#getConfigs(org.xhome.xauth
	 *      .User, org.xhome.db.query.QueryBase)
	 */
	@Override
	public List<Config> getConfigs(User oper, QueryBase query) {
		if (!this.beforeConfigManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query configs, but it's blocked");
			}

			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterConfigManage(oper, Action.QUERY, Status.BLOCKED, null,
					query);
			return null;
		}

		List<Config> results = configDAO.queryConfigs(query);
		if (query != null) {
			query.setResults(results);
			long total = configDAO.countConfigs(query);
			query.setTotal(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query configs with parameters {}",
						query.getParameters());
			} else {
				logger.debug("query configs");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterConfigManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}

	/**
	 * @see org.xhome.xauth.core.service.ConfigService#countConfigs(org.xhome.xauth
	 *      .User, org.xhome.db.query.QueryBase)
	 */
	@Override
	public long countConfigs(User oper, QueryBase query) {
		if (!this.beforeConfigManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count configs, but it's blocked");
			}

			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterConfigManage(oper, Action.COUNT, Status.BLOCKED, null,
					query);
			return -1;
		}

		long c = configDAO.countConfigs(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count configs with parameters {} of {}",
						query.getParameters(), c);
			} else {
				logger.debug("count configs of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterConfigManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}

	/**
	 * @see org.xhome.xauth.core.service.ConfigService#getBaseURL()
	 */
	@Override
	public String getBaseURL() {
		Config config = configDAO.queryConfigByItem(ITEM_BASE_URL);
		return config != null ? config.getValue() : null;
	}

	private void logManage(String content, Short action, Long obj,
			Short status, User oper) {
		ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XAUTH,
				content, action, ManageLog.TYPE_CONFIG, obj,
				oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}

	private boolean beforeConfigManage(User oper, short action, Config config,
			Object... args) {
		if (configManageListeners != null) {
			for (ConfigManageListener listener : configManageListeners) {
				if (!listener.beforeConfigManage(oper, action, config, args)) {
					return false;
				}
			}
		}
		return true;
	}

	private void afterConfigManage(User oper, short action, short result,
			Config config, Object... args) {
		if (configManageListeners != null) {
			for (ConfigManageListener listener : configManageListeners) {
				listener.afterConfigManage(oper, action, result, config, args);
			}
		}
	}

	public void setConfigDAO(ConfigDAO configDAO) {
		this.configDAO = configDAO;
	}

	public ConfigDAO getConfigDAO() {
		return this.configDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setConfigManageListeners(
			List<ConfigManageListener> configManageListeners) {
		this.configManageListeners = configManageListeners;
	}

	public List<ConfigManageListener> getConfigManageListeners() {
		return configManageListeners;
	}

	public void registerConfigManageListener(
			ConfigManageListener configManageListener) {
		if (configManageListeners == null) {
			configManageListeners = new ArrayList<ConfigManageListener>();
		}
		configManageListeners.add(configManageListener);
	}

}
