package org.xhome.validator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @project xxauth-web
 * @xauthor jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:36:57 PM
 * @description 校验器配置
 */
public class AuthValidatorConfig implements Config {

	// 角色校验器配置项
	public final static String ROLE_NAME_EMPTY_MESSAGE = "xauth_role_name_empty_message";
	public final static String ROLE_NAME_SIZE_MIN = "xauth_role_name_size_min";
	public final static String ROLE_NAME_SIZE_MAX = "xauth_role_name_size_max";
	public final static String ROLE_NAME_SIZE_MESSAGE = "xauth_role_name_size_message";
	public final static String ROLE_NAME_PATTERN_REGEXP = "xauth_role_name_pattern_regexp";
	public final static String ROLE_NAME_PATTERN_MESSAGE = "xauth_role_name_pattern_message";

	// 用户校验器配置项
	public final static String USER_NAME_EMPTY_MESSAGE = "xauth_user_name_empty_message";
	public final static String USER_NAME_SIZE_MIN = "xauth_user_name_size_min";
	public final static String USER_NAME_SIZE_MAX = "xauth_user_name_size_max";
	public final static String USER_NAME_SIZE_MESSAGE = "xauth_user_name_size_message";
	public final static String USER_NAME_PATTERN_REGEXP = "xauth_user_name_pattern_regexp";
	public final static String USER_NAME_PATTERN_MESSAGE = "xauth_user_name_pattern_message";

	public final static String USER_METHOD_SIZE_MIN = "xauth_user_method_size_min";
	public final static String USER_METHOD_SIZE_MAX = "xauth_user_method_size_max";
	public final static String USER_METHOD_SIZE_MESSAGE = "xauth_user_method_size_message";

	public final static String USER_NICK_EMPTY_MESSAGE = "xauth_user_nick_empty_message";
	public final static String USER_NICK_SIZE_MIN = "xauth_user_nick_size_min";
	public final static String USER_NICK_SIZE_MAX = "xauth_user_nick_size_max";
	public final static String USER_NICK_SIZE_MESSAGE = "xauth_user_nick_size_message";

	public final static String USER_PASSWORD_EMPTY_MESSAGE = "xauth_user_password_empty_message";
	public final static String USER_PASSWORD_SIZE_MIN = "xauth_user_password_size_min";
	public final static String USER_PASSWORD_SIZE_MAX = "xauth_user_password_size_max";
	public final static String USER_PASSWORD_SIZE_MESSAGE = "xauth_user_password_size_message";
	public final static String USER_PASSWORD_PATTERN_REGEXP = "xauth_user_password_pattern_regexp";
	public final static String USER_PASSWORD_PATTERN_MESSAGE = "xauth_user_password_pattern_message";

	public final static String USER_EMAIL_EMPTY_MESSAGE = "xauth_user_email_empty_message";
	public final static String USER_EMAIL_SIZE_MIN = "xauth_user_email_size_min";
	public final static String USER_EMAIL_SIZE_MAX = "xauth_user_email_size_max";
	public final static String USER_EMAIL_SIZE_MESSAGE = "xauth_user_email_size_message";
	public final static String USER_EMAIL_PATTERN_REGEXP = "xauth_user_email_pattern_regexp";
	public final static String USER_EMAIL_PATTERN_MESSAGE = "xauth_user_email_pattern_message";

	public final static String USER_ROLES_EMPTY_MESSAGE = "xauth_user_roles_empty_message";

	// 配置项校验器配置项
	public final static String CONFIG_ITEM_EMPTY_MESSAGE = "xauth_config_item_empty_message";
	public final static String CONFIG_ITEM_SIZE_MIN = "xauth_config_item_size_min";
	public final static String CONFIG_ITEM_SIZE_MAX = "xauth_config_item_size_max";
	public final static String CONFIG_ITEM_SIZE_MESSAGE = "xauth_config_item_size_message";

	public final static String CONFIG_DISPLAY_EMPTY_MESSAGE = "xauth_config_display_empty_message";
	public final static String CONFIG_DISPLAY_SIZE_MIN = "xauth_config_display_size_min";
	public final static String CONFIG_DISPLAY_SIZE_MAX = "xauth_config_display_size_max";
	public final static String CONFIG_DISPLAY_SIZE_MESSAGE = "xauth_config_display_size_message";

	public final static String CONFIG_VALUE_EMPTY_MESSAGE = "xauth_config_value_empty_message";
	public final static String CONFIG_VALUE_SIZE_MIN = "xauth_config_value_size_min";
	public final static String CONFIG_VALUE_SIZE_MAX = "xauth_config_value_size_max";
	public final static String CONFIG_VALUE_SIZE_MESSAGE = "xauth_config_value_size_message";
	public final static String CONFIG_VALUE_SWITCH_MESSAGE = "xauth_config_value_switch_message";
	public final static String CONFIG_VALUE_NUMBER_MESSAGE = "xauth_config_value_number_message";

	/**
	 * @see org.xhome.validator.config.Config#validatorConfigs()
	 */
	@Override
	public Map<String, String> validatorConfigs() {
		Map<String, String> configs = new HashMap<String, String>();
		// 角色校验器配置项
		configs.put(ROLE_NAME_EMPTY_MESSAGE, "角色名称不能为空");
		configs.put(ROLE_NAME_SIZE_MIN, "4");
		configs.put(ROLE_NAME_SIZE_MAX, "20");
		configs.put(ROLE_NAME_SIZE_MESSAGE, "角色名称必须为4到20个字符");
		configs.put(ROLE_NAME_PATTERN_REGEXP, "^[\\w-_]{4,20}$");
		configs.put(ROLE_NAME_PATTERN_MESSAGE, "角色名称只能包含字母、数字或-_");

		// 用户校验器配置项
		configs.put(USER_NAME_EMPTY_MESSAGE, "用户名不能为空");
		configs.put(USER_NAME_SIZE_MIN, "4");
		configs.put(USER_NAME_SIZE_MAX, "20");
		configs.put(USER_NAME_SIZE_MESSAGE, "用户名必须为4到20个字符");
		configs.put(USER_NAME_PATTERN_REGEXP, "^[\\w-_]{4,20}$");
		configs.put(USER_NAME_PATTERN_MESSAGE, "用户名只能包含字母、数字或-_");

		configs.put(USER_METHOD_SIZE_MIN, "1");
		configs.put(USER_METHOD_SIZE_MAX, "10");
		configs.put(USER_METHOD_SIZE_MESSAGE, "认证方式不能超过10个字符");

		configs.put(USER_NICK_EMPTY_MESSAGE, "用户昵称不能为空");
		configs.put(USER_NICK_SIZE_MIN, "1");
		configs.put(USER_NICK_SIZE_MAX, "20");
		configs.put(USER_NICK_SIZE_MESSAGE, "用户昵称不能超过20个字符");

		configs.put(USER_PASSWORD_EMPTY_MESSAGE, "用户密码不能为空");
		configs.put(USER_PASSWORD_SIZE_MIN, "4");
		configs.put(USER_PASSWORD_SIZE_MAX, "20");
		configs.put(USER_PASSWORD_SIZE_MESSAGE, "用户密码必须为4到20个字符");
		configs.put(USER_PASSWORD_PATTERN_REGEXP, "^[\\w-_]{4,20}$");
		configs.put(USER_PASSWORD_PATTERN_MESSAGE, "用户密码只能包含字母、数字或-_");

		configs.put(USER_EMAIL_EMPTY_MESSAGE, "用户邮箱不能为空");
		configs.put(USER_EMAIL_SIZE_MIN, "1");
		configs.put(USER_EMAIL_SIZE_MAX, "50");
		configs.put(USER_EMAIL_SIZE_MESSAGE, "用户邮箱不能超过50个字符");
		configs.put(
				USER_EMAIL_PATTERN_REGEXP,
				"\\b(^['\\w-_]+(\\.['\\w-_]+)*@([\\w-])+(\\.[\\w-]+)*((\\.[\\w]{2,})|(\\.[\\w]{2,}\\.[\\w]{2,}))$)\\b");
		configs.put(USER_EMAIL_PATTERN_MESSAGE, "用户邮箱格式不正确");

		configs.put(USER_ROLES_EMPTY_MESSAGE, "用户角色不能为空");

		// 配置项校验器配置项
		configs.put(CONFIG_ITEM_EMPTY_MESSAGE, "配置项不能为空");
		configs.put(CONFIG_ITEM_SIZE_MIN, "1");
		configs.put(CONFIG_ITEM_SIZE_MAX, "30");
		configs.put(CONFIG_ITEM_SIZE_MESSAGE, "配置项不能超过30个字符");

		configs.put(CONFIG_DISPLAY_EMPTY_MESSAGE, "显示名称不能为空");
		configs.put(CONFIG_DISPLAY_SIZE_MIN, "1");
		configs.put(CONFIG_DISPLAY_SIZE_MAX, "30");
		configs.put(CONFIG_DISPLAY_SIZE_MESSAGE, "显示名称不能超过30个字符");

		configs.put(CONFIG_VALUE_EMPTY_MESSAGE, "配置值不能为空");
		configs.put(CONFIG_VALUE_SIZE_MIN, "1");
		configs.put(CONFIG_VALUE_SIZE_MAX, "1000");
		configs.put(CONFIG_VALUE_SIZE_MESSAGE, "配置值不能超过1000个字符");
		configs.put(CONFIG_VALUE_SWITCH_MESSAGE, "开关配置项格式错误，只能是0或1");
		configs.put(CONFIG_VALUE_NUMBER_MESSAGE, "数字配置项格式错误");

		return configs;
	}

}
