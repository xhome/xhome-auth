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
 * @date Aug 14, 201310:50:08 PM
 * @description 
 */
public class UserMethodValidator extends Validator {
	
	public final static String	FIELD_METHOD			= "method";
	
	public final static String	CODE_METHOD_EMPTY		= "method.empty";
	public final static String	CODE_METHOD_SIZE		= "method.size";
	public final static String	CODE_METHOD_PATTERN	= "method.pattern";

	@Override
	public boolean validate(Object target, Errors errors) {
		User user = (User) target;
		String method = user == null ? null : user.getName();
		if (StringUtils.isNotEmpty(method)) {
			int size = method.length();
			int min = Integer.parseInt(validationConfig.getConfig(AuthValidatorConfig.USER_METHOD_SIZE_MIN)), 
				max = Integer.parseInt(validationConfig.getConfig(AuthValidatorConfig.USER_METHOD_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(FIELD_METHOD, CODE_METHOD_SIZE, validationConfig.getConfig(AuthValidatorConfig.USER_METHOD_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

}
