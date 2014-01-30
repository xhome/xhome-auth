package org.xhome.xauth.web.validator;

import org.springframework.validation.Errors;
import org.xhome.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.validator.config.AuthValidatorConfig;
import org.xhome.xauth.Config;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Jan 30, 2014
 * @describe 配置项开关校验器，配置值只能是0或1
 */
public class ConfigSwitchValidator extends Validator {

	public final static String CODE_VALUE_SWITCH = "value.switch";

	/**
	 * @see org.xhome.validator.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	@Override
	public boolean validate(Object target, Errors errors) {
		Config config = (Config) target;
		String value = config == null ? null : config.getValue();
		if (StringUtils.isEmpty(value)) {
			errors.rejectValue(
					ConfigValueValidator.FIELD_VALUE,
					ConfigValueValidator.CODE_VALUE_EMPTY,
					validationConfig
							.getConfig(AuthValidatorConfig.CONFIG_VALUE_EMPTY_MESSAGE));
		} else {
			if ("0".equals(value) || "1".equals(value)) {
				return true;
			} else {
				errors.rejectValue(
						ConfigValueValidator.FIELD_VALUE,
						CODE_VALUE_SWITCH,
						validationConfig
								.getConfig(AuthValidatorConfig.CONFIG_VALUE_SWITCH_MESSAGE));
			}
		}
		return false;
	}

}
