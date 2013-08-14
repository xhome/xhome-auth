package org.xhome.xauth.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.Validator;
import org.xhome.validator.config.AuthValidatorConfig;
import org.xhome.xauth.Role;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 
 */
public class RoleTipValidator extends Validator {

	public final static String	FIELD_TIP		= "tip";
	
	public final static String	CODE_TIP_EMPTY	= "tip.empty";
	public final static String	CODE_TIP_SIZE	= "tip.size";
	
	@Override
	public boolean validate(Object target, Errors errors) {
		Role role = (Role) target;
		Long tip = role == null ? null : role.getTip();
		if (tip == null) {
			errors.rejectValue(FIELD_TIP, CODE_TIP_EMPTY, validationConfig.getConfig(AuthValidatorConfig.ROLE_TIP_EMPTY_MESSAGE));
		} else {
			long min = Long.parseLong(validationConfig.getConfig(AuthValidatorConfig.ROLE_TIP_SIZE_MIN)),
				max = Long.parseLong(validationConfig.getConfig(AuthValidatorConfig.ROLE_TIP_SIZE_MAX));
			if (tip < min || tip> max) {
				errors.rejectValue(FIELD_TIP, CODE_TIP_SIZE, validationConfig.getConfig(AuthValidatorConfig.ROLE_TIP_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

}
