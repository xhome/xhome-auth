package org.xhome.xauth.web.validator;

import org.springframework.validation.Errors;
import org.xhome.common.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.validator.config.AuthValidatorConfig;
import org.xhome.xauth.User;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 用户名校验器
 */
public class UserNameValidator extends Validator {

	public final static String FIELD_NAME = "name";

	public final static String CODE_NAME_EMPTY = "name.empty";
	public final static String CODE_NAME_SIZE = "name.size";
	public final static String CODE_NAME_PATTERN = "name.pattern";

	@Override
	public boolean validate(Object target, Errors errors) {
		User user = (User) target;
		String name = user == null ? null : user.getName();
		if (StringUtils.isEmpty(name)) {
			errors.rejectValue(FIELD_NAME, CODE_NAME_EMPTY, validationConfig
					.getConfig(AuthValidatorConfig.USER_NAME_EMPTY_MESSAGE));
		} else {
			int size = name.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(AuthValidatorConfig.USER_NAME_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(AuthValidatorConfig.USER_NAME_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(FIELD_NAME, CODE_NAME_SIZE, validationConfig
						.getConfig(AuthValidatorConfig.USER_NAME_SIZE_MESSAGE));
			} else {
				if (!name
						.matches(validationConfig
								.getConfig(AuthValidatorConfig.USER_NAME_PATTERN_REGEXP))) {
					errors.rejectValue(
							FIELD_NAME,
							CODE_NAME_PATTERN,
							validationConfig
									.getConfig(AuthValidatorConfig.USER_NAME_PATTERN_MESSAGE));
				} else {
					return true;
				}
			}
		}
		return false;
	}

}
