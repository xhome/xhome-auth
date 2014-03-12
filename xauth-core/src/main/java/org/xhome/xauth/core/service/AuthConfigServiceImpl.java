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

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getSMTPHost()
	 */
	@Override
	public String getSMTPHost() {
		Config config = configDAO.queryConfigByItem(ITEM_SMTP_HOST);
		return config != null ? config.getValue() : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getSMTPPort()
	 */
	@Override
	public int getSMTPPort() {
		Config config = configDAO.queryConfigByItem(ITEM_SMTP_PORT);
		return config != null ? Integer.parseInt(config.getValue()) : -1;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getSMTPUserName()
	 */
	@Override
	public String getSMTPUserName() {
		Config config = configDAO.queryConfigByItem(ITEM_SMTP_USERNAME);
		return config != null ? config.getValue() : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getSMTPPassword()
	 */
	@Override
	public String getSMTPPassword() {
		Config config = configDAO.queryConfigByItem(ITEM_SMTP_PASSWORD);
		return config != null ? config.getValue() : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getSMTPFrom()
	 */
	@Override
	public String getSMTPFrom() {
		Config config = configDAO.queryConfigByItem(ITEM_SMTP_FROM);
		return config != null ? config.getValue() : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#enableSMTPSSL()
	 */
	@Override
	public boolean enableSMTPSSL() {
		Config config = configDAO.queryConfigByItem(ITEM_SMTP_SSL);
		return config != null ? !"0".equals(config.getValue()) : false;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getResetPasswordSubject()
	 */
	@Override
	public String getResetPasswordSubject() {
		Config config = configDAO
				.queryConfigByItem(ITEM_RESET_PASSWORD_SUBJECT);
		return config != null ? config.getValue() : null;
	}

	/**
	 * @see org.xhome.xauth.core.service.AuthConfigService#getResetPasswordTemplate()
	 */
	@Override
	public String getResetPasswordTemplate() {
		Config config = configDAO
				.queryConfigByItem(ITEM_RESET_PASSWORD_TEMPLATE);
		return config != null ? config.getValue() : null;
	}

}
