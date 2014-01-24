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
 * @date Jan 25, 2014
 * @describe
 */
public class ConfigValueValidator extends Validator {

	public final static String FIELD_VALUE = "value";

	public final static String CODE_VALUE_EMPTY = "value.empty";
	public final static String CODE_VALUE_SIZE = "value.size";

	@Override
	public boolean validate(Object target, Errors errors) {
		Config config = (Config) target;
		String value = config == null ? null : config.getValue();
		if (StringUtils.isEmpty(value)) {
			errors.rejectValue(FIELD_VALUE, CODE_VALUE_EMPTY, validationConfig
					.getConfig(AuthValidatorConfig.CONFIG_VALUE_EMPTY_MESSAGE));
		} else {
			int size = value.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(AuthValidatorConfig.CONFIG_VALUE_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(AuthValidatorConfig.CONFIG_VALUE_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(
						FIELD_VALUE,
						CODE_VALUE_SIZE,
						validationConfig
								.getConfig(AuthValidatorConfig.CONFIG_VALUE_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

}
