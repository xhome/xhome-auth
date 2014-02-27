package org.xhome.xauth.core.service;

import org.springframework.stereotype.Service;
import org.xhome.xauth.Config;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Jan 28, 2014
 * @describe 认证管理参数查询
 */
@Service
public class AuthConfigServiceImpl extends ConfigServiceImpl implements
		AuthConfigService {

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getNextPage()
	 */
	@Override
	public String getNextPage() {
		Config config = configDAO.queryConfigByItem(ITEM_NEXT_PAGE);
		return config != null ? config.getValue() : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getAuthTryLimit()
	 */
	@Override
	public int getAuthTryLimit() {
		Config config = configDAO.queryConfigByItem(ITEM_TRY_LIMIT);
		return config != null ? Integer.parseInt(config.getValue()) : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getAuthLockTime()
	 */
	@Override
	public long getAuthLockTime() {
		Config config = configDAO.queryConfigByItem(ITEM_LOCK_TIME);
		return config != null ? Long.parseLong(config.getValue()) : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#allowAuthLog()
	 */
	@Override
	public boolean allowAuthLog() {
		Config config = configDAO.queryConfigByItem(ITEM_ALLOW_AUTH_LOG);
		return config != null ? !"0".equals(config.getValue()) : false;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#allowManageLog()
	 */
	@Override
	public boolean allowManageLog() {
		Config config = configDAO.queryConfigByItem(ITEM_ALLOW_MANAGE_LOG);
		return config != null ? !"0".equals(config.getValue()) : false;
	}

}
