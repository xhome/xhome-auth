package org.xhome.xauth.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.xhome.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.response.DataResult;
import org.xhome.xauth.Config;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ConfigService;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xauth.web.validator.ConfigNumberValidator;
import org.xhome.xauth.web.validator.ConfigSwitchValidator;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Jan 24, 2014
 * @describe 配置项管理Action
 */
@Controller
public class ConfigAction extends AbstractAction {

	@Autowired
	private ConfigService configService;
	@Autowired(required = false)
	private Map<String, String> configValidator; // item - validator class name
													// 配置项更新校验器

	public final static String RM_CONFIG_UPDATE = "xauth/config/update";

	public final static String RM_CONFIG_GET = "xauth/config/get";
	public final static String RM_CONFIG_QUERY = "xauth/config/query";
	public final static String RM_CONFIG_COUNT = "xauth/config/count";

	public ConfigAction() {
		super();
		this.configValidator = new HashMap<String, String>();
		String switchValidator = ConfigSwitchValidator.class.getName();
		String numberValidator = ConfigNumberValidator.class.getName();
		this.configValidator.put("allow_auth_log", switchValidator);
		this.configValidator.put("allow_manage_log", switchValidator);
		this.configValidator.put("auth_lock_time", numberValidator);
		this.configValidator.put("auth_try_limit", numberValidator);
	}

	@RequestMapping(value = RM_CONFIG_UPDATE, method = RequestMethod.POST)
	public Object updateConfig(
			@Validated @RequestAttribute("config") Config config,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;
		CommonResult result = null;

		// 如果针对配置项设置了校验器，需要先进行校验
		String validatorName = this.configValidator.get(config.getItem());
		if (StringUtils.isNotEmpty(validatorName)) {
			Validator validator = super.validatorMapping
					.getValidatorByName(validatorName);
			BindException be = new BindException(config, "config");
			validator.validate(config, be);
			if (be.hasErrors()) {
				result = errorResult(be);
				status = result.getStatus();
				msg = result.getMessage();
			}
		}

		User user = AuthUtils.getCurrentUser(request);
		if (status == Status.SUCCESS) {
			AuthUtils.setModifier(request, config);
			status = (short) configService.updateConfig(user, config);
			if (status == Status.SUCCESS) {
				msg = "更新配置项[" + config.getDisplay() + "]成功";
			} else {
				msg = "更新配置项[" + config.getDisplay() + "]失败";
			}
			result = new CommonResult(status, msg, config);
		}

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}

		return result;
	}

	// @RequestMapping(value = RM_CONFIG_GET, method = RequestMethod.GET)
	public Object getConfig(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "item", required = false) String item,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		Config config = null;
		if (id != null) {
			logger.info("用户" + uname + "按ID[" + id + "]查询配置项");
			config = configService.getConfig(user, id);
		} else if (StringUtils.isNotEmpty(item)) {
			logger.info("用户" + uname + "按名称[" + item + "]查询配置项");
			config = configService.getConfig(user, item);
		}

		String msg = null;
		short status = Status.SUCCESS;

		if (config != null) {
			msg = "配置项[" + config.getId() + "]" + config.getItem() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "配置项查询失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}

		return new CommonResult(status, msg, config);
	}

	@RequestMapping(value = RM_CONFIG_QUERY, method = RequestMethod.GET)
	public Object getConfigs(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters()
						+ "查询配置项信息");
			} else {
				query = new QueryBase();
				logger.info("用户" + uname + "查询配置项信息");
			}
		}
		configService.getConfigs(user, query);

		String msg = "条件查询配置项信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}

		return new DataResult(status, msg, query);
	}

	// @RequestMapping(value = RM_CONFIG_COUNT, method = RequestMethod.GET)
	public Object countConfigs(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters()
						+ "统计配置项信息");
			} else {
				logger.info("用户" + uname + "统计配置项信息");
			}
		}
		long count = configService.countConfigs(user, query);

		String msg = "条件统计配置项信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public Map<String, String> getConfigValidator() {
		return configValidator;
	}

	public void setConfigValidator(Map<String, String> configValidator) {
		this.configValidator = configValidator;
	}

	public void registerConfigValidator(String item, String validator) {
		this.configValidator.put(item, validator);
	}

}