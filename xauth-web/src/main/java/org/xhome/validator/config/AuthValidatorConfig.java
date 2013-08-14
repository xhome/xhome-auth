package org.xhome.validator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @project xxauth-web
 * @xauthor jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:36:57 PM
 * @description 
 */
public class AuthValidatorConfig implements Config {
	
	public final static String		ROLE_NAME_EMPTY_MESSAGE			= "xauth_role_name_empty_message";
	public final static String		ROLE_NAME_SIZE_MIN				= "xauth_role_name_size_min";
	public final static String		ROLE_NAME_SIZE_MAX				= "xauth_role_name_size_max";
	public final static String		ROLE_NAME_SIZE_MESSAGE			= "xauth_role_name_size_message";
	public final static String		ROLE_NAME_PATTERN_REGEXP		= "xauth_role_name_pattern_regexp";
	public final static String		ROLE_NAME_PATTERN_MESSAGE		= "xauth_role_name_pattern_message";
	
	public final static String		ROLE_TIP_EMPTY_MESSAGE			= "xauth_role_tip_empty_message";
	public final static String		ROLE_TIP_SIZE_MIN				= "xauth_role_tip_size_min";
	public final static String		ROLE_TIP_SIZE_MAX				= "xauth_role_tip_size_max";
	public final static String		ROLE_TIP_SIZE_MESSAGE			= "xauth_role_tip_size_message";
	
	public final static String		USER_NAME_EMPTY_MESSAGE			= "xauth_user_name_empty_message";
	public final static String		USER_NAME_SIZE_MIN				= "xauth_user_name_size_min";
	public final static String		USER_NAME_SIZE_MAX				= "xauth_user_name_size_max";
	public final static String		USER_NAME_SIZE_MESSAGE			= "xauth_user_name_size_message";
	public final static String		USER_NAME_PATTERN_REGEXP		= "xauth_user_name_pattern_regexp";
	public final static String		USER_NAME_PATTERN_MESSAGE		= "xauth_user_name_pattern_message";
	
	public final static String		USER_METHOD_SIZE_MIN			= "xauth_user_method_size_min";
	public final static String		USER_METHOD_SIZE_MAX			= "xauth_user_method_size_max";
	public final static String		USER_METHOD_SIZE_MESSAGE		= "xauth_user_method_size_message";
	
	public final static String		USER_NICK_EMPTY_MESSAGE			= "xauth_user_nick_empty_message";
	public final static String		USER_NICK_SIZE_MIN				= "xauth_user_nick_size_min";
	public final static String		USER_NICK_SIZE_MAX				= "xauth_user_nick_size_max";
	public final static String		USER_NICK_SIZE_MESSAGE			= "xauth_user_nick_size_message";
	
	public final static String		USER_PASSWORD_EMPTY_MESSAGE		= "xauth_user_password_empty_message";
	public final static String		USER_PASSWORD_SIZE_MIN			= "xauth_user_password_size_min";
	public final static String		USER_PASSWORD_SIZE_MAX			= "xauth_user_password_size_max";
	public final static String		USER_PASSWORD_SIZE_MESSAGE		= "xauth_user_password_size_message";
	public final static String		USER_PASSWORD_PATTERN_REGEXP	= "xauth_user_password_pattern_regexp";
	public final static String		USER_PASSWORD_PATTERN_MESSAGE	= "xauth_user_password_pattern_message";
	
	public final static String		USER_EMAIL_EMPTY_MESSAGE		= "xauth_user_email_empty_message";
	public final static String		USER_EMAIL_SIZE_MIN				= "xauth_user_email_size_min";
	public final static String		USER_EMAIL_SIZE_MAX				= "xauth_user_email_size_max";
	public final static String		USER_EMAIL_SIZE_MESSAGE			= "xauth_user_email_size_message";
	public final static String		USER_EMAIL_PATTERN_REGEXP		= "xauth_user_email_pattern_regexp";
	public final static String		USER_EMAIL_PATTERN_MESSAGE		= "xauth_user_email_pattern_message";
	
	public final static String		USER_ROLES_EMPTY_MESSAGE		= "xauth_user_roles_empty_message";

	@Override
	public Map<String, String> validatorConfigs() {
		Map<String, String> configs = new HashMap<String, String>();
		
		configs.put(ROLE_NAME_EMPTY_MESSAGE, "角色名不能为空");
		configs.put(ROLE_NAME_SIZE_MIN, "4");
		configs.put(ROLE_NAME_SIZE_MAX, "20");
		configs.put(ROLE_NAME_SIZE_MESSAGE, "角色名必须为4到20个字符");
		configs.put(ROLE_NAME_PATTERN_REGEXP, "^[\\w-_]{4,20}$");
		configs.put(ROLE_NAME_PATTERN_MESSAGE, "角色名只能包含字母、数字或-_");
		
		configs.put(ROLE_TIP_EMPTY_MESSAGE, "角色标识不能为空");
		configs.put(ROLE_TIP_SIZE_MIN, "0");
		configs.put(ROLE_TIP_SIZE_MAX, "" + Long.MAX_VALUE);
		configs.put(ROLE_TIP_SIZE_MESSAGE, "角色标识必须大于0");
		
		configs.put(USER_NAME_EMPTY_MESSAGE, "用户名不能为空");
		configs.put(USER_NAME_SIZE_MIN, "4");
		configs.put(USER_NAME_SIZE_MAX, "20");
		configs.put(USER_NAME_SIZE_MESSAGE, "用户名必须为4到20个字符");
		configs.put(USER_NAME_PATTERN_REGEXP, "^[\\w-_]{4,20}$");
		configs.put(USER_NAME_PATTERN_MESSAGE, "用户名只能包含字母、数字或-_");
		
		configs.put(USER_METHOD_SIZE_MIN, "0");
		configs.put(USER_METHOD_SIZE_MAX, "10");
		configs.put(USER_METHOD_SIZE_MESSAGE, "认证方式不能超过10个字符");
		
		configs.put(USER_NICK_EMPTY_MESSAGE, "用户昵称不能为空");
		configs.put(USER_NICK_SIZE_MIN, "0");
		configs.put(USER_NICK_SIZE_MAX, "20");
		configs.put(USER_NICK_SIZE_MESSAGE, "用户昵称不能超过20个字符");
		
		configs.put(USER_PASSWORD_EMPTY_MESSAGE, "用户密码不能为空");
		configs.put(USER_PASSWORD_SIZE_MIN, "4");
		configs.put(USER_PASSWORD_SIZE_MAX, "20");
		configs.put(USER_PASSWORD_SIZE_MESSAGE, "用户密码必须为4到20个字符");
		configs.put(USER_PASSWORD_PATTERN_REGEXP, "^[\\w-_]{4,20}$");
		configs.put(USER_PASSWORD_PATTERN_MESSAGE, "用户密码只能包含字母、数字或-_");
		
		configs.put(USER_EMAIL_EMPTY_MESSAGE, "用户邮箱不能为空");
		configs.put(USER_EMAIL_SIZE_MIN, "0");
		configs.put(USER_EMAIL_SIZE_MAX, "50");
		configs.put(USER_EMAIL_SIZE_MESSAGE, "用户邮箱不能超过50个字符");
		configs.put(USER_EMAIL_PATTERN_REGEXP, "\\b(^['\\w-_]+(\\.['\\w-_]+)*@([\\w-])+(\\.[\\w-]+)*((\\.[\\w]{2,})|(\\.[\\w]{2,}\\.[\\w]{2,}))$)\\b");
		configs.put(USER_EMAIL_PATTERN_MESSAGE, "用户邮箱格式不正确");
		
		configs.put(USER_ROLES_EMPTY_MESSAGE, "用户角色不能为空");
		
		return configs;
	}

}
