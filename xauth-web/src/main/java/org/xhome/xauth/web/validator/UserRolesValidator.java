package org.xhome.xauth.web.validator;

import java.util.List;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.AuthValidatorConfig;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 用户角色校验器
 */
public class UserRolesValidator extends Validator {

	private Validator idValidator;
	private Validator roleNameValidator;

	public final static String FIELD_ROLES = "roles";

	public final static String CODE_ROLES_EMPTY = "roles.empty";

	public UserRolesValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class
				.getName());
		roleNameValidator = validatorMapping
				.getValidatorByName(RoleNameValidator.class.getName());
	}

	@Override
	public boolean validate(Object target, Errors errors) {
		User user = (User) target;
		List<Role> roles = user == null ? null : user.getRoles();
		if (roles == null || roles.isEmpty()) {
			errors.rejectValue(FIELD_ROLES, CODE_ROLES_EMPTY, validationConfig
					.getConfig(AuthValidatorConfig.USER_ROLES_EMPTY_MESSAGE));
			return false;
		} else {
			for (Role role : roles) {
				if (idValidator.validate(role, errors)
						&& roleNameValidator.validate(role, errors)) {
					continue;
				}
				return false;
			}
			return true;
		}
	}

}
