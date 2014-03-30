package org.xhome.xauth.web.validator;

import org.springframework.validation.Errors;
import org.xhome.common.util.StringUtils;
import org.xhome.validator.config.AuthValidatorConfig;
import org.xhome.xauth.User;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 用户密码校验器
 */
public class UserPasswordNotEmptyValidator extends UserPasswordValidator {

	public final static String CODE_PASSWORD_EMPTY = "password.empty";

	@Override
	public boolean validate(Object target, Errors errors) {
		User user = (User) target;
		String password = user == null ? null : user.getPassword();
		if (StringUtils.isEmpty(password)) {
			errors.rejectValue(
					FIELD_PASSWORD,
					CODE_PASSWORD_EMPTY,
					validationConfig
							.getConfig(AuthValidatorConfig.USER_PASSWORD_EMPTY_MESSAGE));
		} else {
			return super.validate(target, errors);
		}
		return false;
	}

}
