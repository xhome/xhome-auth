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
 * @description 用户密码校验器(后台更新用户信息时允许密码为空)
 */
public class UserPasswordValidator extends Validator {

	public final static String FIELD_PASSWORD = "password";

	public final static String CODE_PASSWORD_SIZE = "password.size";
	public final static String CODE_PASSWORD_PATTERN = "password.pattern";

	@Override
	public boolean validate(Object target, Errors errors) {
		User user = (User) target;
		String password = user == null ? null : user.getPassword();
		if (StringUtils.isNotEmpty(password)) {
			int size = password.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(AuthValidatorConfig.USER_PASSWORD_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(AuthValidatorConfig.USER_PASSWORD_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(
						FIELD_PASSWORD,
						CODE_PASSWORD_SIZE,
						validationConfig
								.getConfig(AuthValidatorConfig.USER_PASSWORD_SIZE_MESSAGE));
			} else {
				if (!password
						.matches(validationConfig
								.getConfig(AuthValidatorConfig.USER_PASSWORD_PATTERN_REGEXP))) {
					errors.rejectValue(
							FIELD_PASSWORD,
							CODE_PASSWORD_PATTERN,
							validationConfig
									.getConfig(AuthValidatorConfig.USER_PASSWORD_PATTERN_MESSAGE));
				} else {
					return true;
				}
			}
		}
		return true;
	}

}
