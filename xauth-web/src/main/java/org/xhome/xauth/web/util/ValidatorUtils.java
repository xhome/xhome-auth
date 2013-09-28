package org.xhome.xauth.web.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.xhome.common.constant.Status;
import org.xhome.http.response.Result;
import org.xhome.validator.IdValidator;
import org.xhome.validator.StatusValidator;
import org.xhome.validator.VersionValidator;
import org.xhome.xauth.web.validator.RoleNameValidator;
import org.xhome.xauth.web.validator.RoleTipValidator;
import org.xhome.xauth.web.validator.UserEmailValidator;
import org.xhome.xauth.web.validator.UserNameValidator;
import org.xhome.xauth.web.validator.UserNickValidator;
import org.xhome.xauth.web.validator.UserPasswordValidator;
import org.xhome.xauth.web.validator.UserRolesValidator;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description code 0 ~ 100 are reversed for xauth-web
 */
public class ValidatorUtils {

	protected final static Map<String, Short> codes;
	
	static {
		codes = new HashMap<String, Short>();
		codes.put(IdValidator.CODE_ID_EMPTY, (short) 10);
		codes.put(VersionValidator.CODE_VERSION_EMPTY, (short) 11);
		codes.put(StatusValidator.CODE_STATUS_EMPTY, (short) 12);
		
		codes.put(RoleNameValidator.CODE_NAME_EMPTY, (short) 13);
		codes.put(RoleNameValidator.CODE_NAME_SIZE, (short) 14);
		codes.put(RoleNameValidator.CODE_NAME_PATTERN, (short) 15);
		codes.put(RoleTipValidator.CODE_TIP_EMPTY, (short) 16);
		codes.put(RoleTipValidator.CODE_TIP_SIZE, (short) 17);
		
		codes.put(UserNameValidator.CODE_NAME_EMPTY, (short) 18);
		codes.put(UserNameValidator.CODE_NAME_SIZE, (short) 19);
		codes.put(UserNameValidator.CODE_NAME_PATTERN, (short) 20);
		codes.put(UserNickValidator.CODE_NICK_EMPTY, (short) 21);
		codes.put(UserNickValidator.CODE_NICK_SIZE, (short) 22);
		codes.put(UserPasswordValidator.CODE_PASSWORD_EMPTY, (short) 23);
		codes.put(UserPasswordValidator.CODE_PASSWORD_SIZE, (short) 24);
		codes.put(UserPasswordValidator.CODE_PASSWORD_PATTERN, (short) 25);
		codes.put(UserEmailValidator.CODE_EMAIL_EMPTY, (short) 26);
		codes.put(UserEmailValidator.CODE_EMAIL_SIZE, (short) 27);
		codes.put(UserEmailValidator.CODE_EMAIL_PATTERN, (short) 28);
		codes.put(UserRolesValidator.CODE_ROLES_EMPTY, (short) 29);
	}
	
	public static Result errorResult(BindingResult result) {
		ObjectError error = result.getAllErrors().get(0);
		return new Result(convertCode(error.getCode()), error.getDefaultMessage());
	}
	
	public static short convertCode(String errorCode) {
		Short s = codes.get(errorCode);
		return s == null ? Status.ERROR : s;
	}
	
}
