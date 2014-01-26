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
public class ConfigDisplayValidator extends Validator {

	public final static String FIELD_DISPLAY = "display";

	public final static String CODE_DISPLAY_EMPTY = "display.empty";
	public final static String CODE_DISPLAY_SIZE = "display.size";

	@Override
	public boolean validate(Object target, Errors errors) {
		Config config = (Config) target;
		String display = config == null ? null : config.getValue();
		if (StringUtils.isEmpty(display)) {
			errors.rejectValue(
					FIELD_DISPLAY,
					CODE_DISPLAY_EMPTY,
					validationConfig
							.getConfig(AuthValidatorConfig.CONFIG_DISPLAY_EMPTY_MESSAGE));
		} else {
			int size = display.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(AuthValidatorConfig.CONFIG_DISPLAY_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(AuthValidatorConfig.CONFIG_DISPLAY_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(
						FIELD_DISPLAY,
						CODE_DISPLAY_SIZE,
						validationConfig
								.getConfig(AuthValidatorConfig.CONFIG_DISPLAY_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

}
