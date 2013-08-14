package org.xhome.validator.mapping;

import java.util.HashMap;
import java.util.Map;

import org.xhome.validator.IdValidator;
import org.xhome.validator.StatusValidator;
import org.xhome.validator.VersionValidator;
import org.xhome.xauth.web.action.RoleAction;
import org.xhome.xauth.web.action.UserAction;
import org.xhome.xauth.web.validator.RoleNameValidator;
import org.xhome.xauth.web.validator.RoleTipValidator;
import org.xhome.xauth.web.validator.UserEmailValidator;
import org.xhome.xauth.web.validator.UserMethodValidator;
import org.xhome.xauth.web.validator.UserNameValidator;
import org.xhome.xauth.web.validator.UserNickValidator;
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

	@Override
	public Map<String, String> validatorMappings() {
		Map<String, String> mappings = new HashMap<String, String>();
		String idValidator = IdValidator.class.getName(),
				versionValidator = VersionValidator.class.getName(),
				baseValidator = idValidator + "," + versionValidator + "," + StatusValidator.class.getName();
		
		String roleNameValidator = RoleNameValidator.class.getName(),
			roleTipValidator = RoleTipValidator.class.getName(),
			roleNameTipValidator = roleNameValidator + "," + roleTipValidator,
			roleIdNameValidator = idValidator + "," + roleNameValidator;
		mappings.put(RoleAction.RM_ROLE_ADD, roleNameTipValidator);
		mappings.put(RoleAction.RM_ROLE_UPDATE, baseValidator + "," + roleNameTipValidator);
		mappings.put(RoleAction.RM_ROLE_LOCK, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_UNLOCK, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_REMOVE, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_DELETE, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_EXISTS, roleNameValidator);
		mappings.put(RoleAction.RM_ROLE_UPDATEABLE, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_LOCKED, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_REMOVEABLE, roleIdNameValidator);
		mappings.put(RoleAction.RM_ROLE_DELETEABLE, roleIdNameValidator);
		
		String userNameValidator = UserNameValidator.class.getName(),
			userPasswordValidator = UserPasswordValidator.class.getName(),
			userMethodValidator = UserMethodValidator.class.getName(),
			userNickValidator = UserNickValidator.class.getName(),
			userEmailValidator = UserEmailValidator.class.getName(),
			userRolesValidator = UserRolesValidator.class.getName(),
			userNamePasswordValidator = userNameValidator + "," + userPasswordValidator,
			userIdNameValidator = idValidator + "," + userNameValidator,
			userNickEmailValidator = userNickValidator + "," + userEmailValidator,
			userValidator = userNamePasswordValidator + "," + userNickEmailValidator;
		mappings.put(UserAction.RM_USER_LOGIN, userNamePasswordValidator);
		mappings.put(UserAction.RM_USER_ADD, userValidator + "," + userRolesValidator + "," + userMethodValidator);
		mappings.put(UserAction.RM_USER_UPDATE, baseValidator + "," + userValidator + "," + userMethodValidator);
		mappings.put(UserAction.RM_USER_CHPASSWD, userPasswordValidator);
		mappings.put(UserAction.RM_USER_LOCK, userIdNameValidator);
		mappings.put(UserAction.RM_USER_UNLOCK, userIdNameValidator);
		mappings.put(UserAction.RM_USER_REMOVE, userIdNameValidator);
		mappings.put(UserAction.RM_USER_DELETE, userIdNameValidator);
		mappings.put(UserAction.RM_USER_EXISTS, userNameValidator);
		mappings.put(UserAction.RM_USER_UPDATEABLE, userIdNameValidator);
		mappings.put(UserAction.RM_USER_LOCKED, userIdNameValidator);
		mappings.put(UserAction.RM_USER_REMOVEABLE, userIdNameValidator);
		mappings.put(UserAction.RM_USER_DELETEABLE, userIdNameValidator);
		
		String userRoleValidator = userIdNameValidator + "," + userRolesValidator;
		mappings.put(UserAction.RM_USER_ROLE_ADD, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_LOCK, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_UNLOCK, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_REMOVE, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_DELETE, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_EXISTS, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_UPDATEABLE, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_LOCKED, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_REMOVEABLE, userRoleValidator);
		mappings.put(UserAction.RM_USER_ROLE_DELETEABLE, userRoleValidator);

		return mappings;
	}

}
