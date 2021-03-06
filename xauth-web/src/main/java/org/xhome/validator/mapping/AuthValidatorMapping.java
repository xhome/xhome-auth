package org.xhome.validator.mapping;

import java.util.HashMap;
import java.util.Map;

import org.xhome.validator.IdValidator;
import org.xhome.validator.StatusValidator;
import org.xhome.validator.VersionValidator;
import org.xhome.xauth.web.action.ConfigAction;
import org.xhome.xauth.web.action.RoleAction;
import org.xhome.xauth.web.action.UserAction;
import org.xhome.xauth.web.validator.ConfigDisplayValidator;
import org.xhome.xauth.web.validator.ConfigItemValidator;
import org.xhome.xauth.web.validator.ConfigValueValidator;
import org.xhome.xauth.web.validator.RoleNameValidator;
import org.xhome.xauth.web.validator.UserEmailValidator;
import org.xhome.xauth.web.validator.UserMethodValidator;
import org.xhome.xauth.web.validator.UserNameValidator;
import org.xhome.xauth.web.validator.UserNickValidator;
import org.xhome.xauth.web.validator.UserPasswordNotEmptyValidator;
import org.xhome.xauth.web.validator.UserPasswordValidator;
import org.xhome.xauth.web.validator.UserRolesValidator;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description
 */
public class AuthValidatorMapping implements Mapping {

	/**
	 * @see org.xhome.validator.mapping.Mapping#validatorMappings()
	 */
	@Override
	public Map<String, String> validatorMappings() {
		Map<String, String> mappings = new HashMap<String, String>();
		String idValidator = IdValidator.class.getName(), versionValidator = VersionValidator.class
				.getName(), baseValidator = idValidator + ","
				+ versionValidator + "," + StatusValidator.class.getName();

		String roleNameValidator = RoleNameValidator.class.getName(), roleIdNameValidator = idValidator
				+ "," + roleNameValidator, roleValidator = baseValidator + ","
				+ roleNameValidator;
		mappings.put(RoleAction.RM_ROLE_ADD, roleNameValidator);
		mappings.put(RoleAction.RM_ROLE_UPDATE, roleValidator);
		mappings.put(RoleAction.RM_ROLE_LOCK, roleValidator);
		mappings.put(RoleAction.RM_ROLE_UNLOCK, roleValidator);
		mappings.put(RoleAction.RM_ROLE_DELETE, roleValidator);
		mappings.put(RoleAction.RM_ROLE_EXISTS, roleNameValidator);
		mappings.put(RoleAction.RM_ROLE_UPDATEABLE, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_LOCKED, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_DELETEABLE, roleIdNameValidator);

		String userNameValidator = UserNameValidator.class.getName();
		String userPasswordValidator = UserPasswordValidator.class.getName();
		String userPasswordNotEmptyValidator = UserPasswordNotEmptyValidator.class
				.getName();
		String userMethodValidator = UserMethodValidator.class.getName();
		String userNickValidator = UserNickValidator.class.getName();
		String userEmailValidator = UserEmailValidator.class.getName();
		String userRolesValidator = UserRolesValidator.class.getName();
		String userNamePasswordValidator = userNameValidator + ","
				+ userPasswordNotEmptyValidator;
		String userIdNameValidator = idValidator + "," + userNameValidator;
		String userNickEmailValidator = userNickValidator + ","
				+ userEmailValidator;
		String userValidator = userNickEmailValidator + ","
				+ userRolesValidator + "," + userMethodValidator;
		mappings.put(UserAction.RM_USER_LOGIN, userNamePasswordValidator);
		mappings.put(UserAction.RM_USER_FORGET, userNameValidator);
		mappings.put(UserAction.RM_USER_RESET, userPasswordValidator);
		mappings.put(UserAction.RM_USER_ADD, userNamePasswordValidator + ","
				+ userValidator);
		mappings.put(UserAction.RM_USER_UPDATE, baseValidator + ","
				+ userNameValidator + "," + userPasswordValidator + ","
				+ userValidator);
		mappings.put(UserAction.RM_USER_LOCK, userIdNameValidator);
		mappings.put(UserAction.RM_USER_UNLOCK, userIdNameValidator);
		mappings.put(UserAction.RM_USER_DELETE, userIdNameValidator);
		mappings.put(UserAction.RM_USER_EXISTS, userNameValidator);
		mappings.put(UserAction.RM_USER_UPDATEABLE, userIdNameValidator);
		mappings.put(UserAction.RM_USER_LOCKED, userIdNameValidator);
		mappings.put(UserAction.RM_USER_DELETEABLE, userIdNameValidator);

		String userRoleValidator = userIdNameValidator + ","
				+ userRolesValidator;
		mappings.put(UserAction.RM_USER_ROLE_ADD, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_LOCK, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_UNLOCK, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_DELETE, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_EXISTS, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_UPDATEABLE, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_LOCKED, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_DELETEABLE, userRoleValidator);

		String configValidator = baseValidator + ","
				+ ConfigItemValidator.class.getName() + ","
				+ ConfigDisplayValidator.class.getName() + ","
				+ ConfigValueValidator.class.getName();
		mappings.put(ConfigAction.RM_CONFIG_UPDATE, configValidator);

		return mappings;
	}

	/**
	 * @see org.xhome.validator.mapping.Mapping#codeMappings()
	 */
	@Override
	public Map<String, Short> codeMappings() {
		Map<String, Short> codes = new HashMap<String, Short>();
		codes.put(IdValidator.CODE_ID_EMPTY, (short) 10);
		codes.put(VersionValidator.CODE_VERSION_EMPTY, (short) 11);
		codes.put(StatusValidator.CODE_STATUS_EMPTY, (short) 12);

		codes.put(RoleNameValidator.CODE_NAME_EMPTY, (short) 13);
		codes.put(RoleNameValidator.CODE_NAME_SIZE, (short) 14);
		codes.put(RoleNameValidator.CODE_NAME_PATTERN, (short) 15);

		codes.put(UserNameValidator.CODE_NAME_EMPTY, (short) 16);
		codes.put(UserNameValidator.CODE_NAME_SIZE, (short) 17);
		codes.put(UserNameValidator.CODE_NAME_PATTERN, (short) 18);
		codes.put(UserNickValidator.CODE_NICK_EMPTY, (short) 19);
		codes.put(UserNickValidator.CODE_NICK_SIZE, (short) 20);
		codes.put(UserPasswordNotEmptyValidator.CODE_PASSWORD_EMPTY, (short) 21);
		codes.put(UserPasswordNotEmptyValidator.CODE_PASSWORD_SIZE, (short) 22);
		codes.put(UserPasswordNotEmptyValidator.CODE_PASSWORD_PATTERN,
				(short) 23);
		codes.put(UserEmailValidator.CODE_EMAIL_EMPTY, (short) 24);
		codes.put(UserEmailValidator.CODE_EMAIL_SIZE, (short) 25);
		codes.put(UserEmailValidator.CODE_EMAIL_PATTERN, (short) 26);
		codes.put(UserRolesValidator.CODE_ROLES_EMPTY, (short) 27);

		codes.put(ConfigItemValidator.CODE_ITEM_EMPTY, (short) 28);
		codes.put(ConfigItemValidator.CODE_ITEM_SIZE, (short) 29);
		codes.put(ConfigDisplayValidator.CODE_DISPLAY_EMPTY, (short) 30);
		codes.put(ConfigDisplayValidator.CODE_DISPLAY_SIZE, (short) 31);
		codes.put(ConfigValueValidator.CODE_VALUE_EMPTY, (short) 32);
		codes.put(ConfigValueValidator.CODE_VALUE_SIZE, (short) 33);
		return codes;
	}

}
