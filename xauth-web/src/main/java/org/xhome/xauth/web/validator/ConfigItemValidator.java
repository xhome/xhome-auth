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
 * @date Jan 26, 2014
 * @describe
 */
public class ConfigItemValidator extends Validator {

	public final static String FIELD_ITEM = "item";

	public final static String CODE_ITEM_EMPTY = "item.empty";
	public final static String CODE_ITEM_SIZE = "item.size";

	@Override
	public boolean validate(Object target, Errors errors) {
		Config config = (Config) target;
		String item = config == null ? null : config.getItem();
		if (StringUtils.isEmpty(item)) {
			errors.rejectValue(FIELD_ITEM, CODE_ITEM_EMPTY, validationConfig
					.getConfig(AuthValidatorConfig.CONFIG_ITEM_EMPTY_MESSAGE));
		} else {
			int size = item.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(AuthValidatorConfig.CONFIG_ITEM_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(AuthValidatorConfig.CONFIG_ITEM_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(
						FIELD_ITEM,
						CODE_ITEM_SIZE,
						validationConfig
								.getConfig(AuthValidatorConfig.CONFIG_ITEM_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

}
