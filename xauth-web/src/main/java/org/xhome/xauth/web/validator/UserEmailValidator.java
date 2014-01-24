package org.xhome.xauth.web.validator;

import org.springframework.validation.Errors;
import org.xhome.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.validator.config.AuthValidatorConfig;
import org.xhome.xauth.User;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 用户邮箱校验器
 */
public class UserEmailValidator extends Validator {

	public final static String FIELD_EMAIL = "email";

	public final static String CODE_EMAIL_EMPTY = "email.empty";
	public final static String CODE_EMAIL_SIZE = "email.size";
	public final static String CODE_EMAIL_PATTERN = "email.pattern";

	@Override
	public boolean validate(Object target, Errors errors) {
		User user = (User) target;
		String email = user == null ? null : user.getEmail();
		if (StringUtils.isEmpty(email)) {
			errors.rejectValue(FIELD_EMAIL, CODE_EMAIL_EMPTY, validationConfig
					.getConfig(AuthValidatorConfig.USER_EMAIL_EMPTY_MESSAGE));
		} else {
			int size = email.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(AuthValidatorConfig.USER_EMAIL_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(AuthValidatorConfig.USER_EMAIL_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(
						FIELD_EMAIL,
						CODE_EMAIL_SIZE,
						validationConfig
								.getConfig(AuthValidatorConfig.USER_EMAIL_SIZE_MESSAGE));
			} else {
				if (!email
						.matches(validationConfig
								.getConfig(AuthValidatorConfig.USER_EMAIL_PATTERN_REGEXP))) {
					errors.rejectValue(
							FIELD_EMAIL,
							CODE_EMAIL_PATTERN,
							validationConfig
									.getConfig(AuthValidatorConfig.USER_EMAIL_PATTERN_MESSAGE));
				} else {
					return true;
				}
			}
		}
		return false;
	}

}
