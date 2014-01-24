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
 * @description 用户昵称校验器
 */
public class UserNickValidator extends Validator {

	public final static String FIELD_NICK = "nick";

	public final static String CODE_NICK_EMPTY = "nick.empty";
	public final static String CODE_NICK_SIZE = "nick.size";

	@Override
	public boolean validate(Object target, Errors errors) {
		User user = (User) target;
		String nick = user == null ? null : user.getNick();
		if (StringUtils.isEmpty(nick)) {
			errors.rejectValue(FIELD_NICK, CODE_NICK_EMPTY, validationConfig
					.getConfig(AuthValidatorConfig.USER_NICK_EMPTY_MESSAGE));
		} else {
			int size = nick.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(AuthValidatorConfig.USER_NICK_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(AuthValidatorConfig.USER_NICK_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(FIELD_NICK, CODE_NICK_SIZE, validationConfig
						.getConfig(AuthValidatorConfig.USER_NICK_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

}
