package org.xhome.xauth.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.common.constant.Agent;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.http.response.Result;
import org.xhome.http.util.RequestUtils;
import org.xhome.http.util.ResponseUtils;
import org.xhome.http.util.SessionUtils;
import org.xhome.util.StringUtils;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.AuthException;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.UserService;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xauth.web.util.ValidatorUtils;
import org.xhome.xauth.web.validator.UserPasswordValidator;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:50:23 PM
 * @description 
 */
@Controller
public class UserAction {
	
	@Autowired
	private UserService userService;
	private Logger logger = LoggerFactory.getLogger(UserAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_USER_AUTH_CODE		= "xauth/user/authcode.do";
	public final static String	RM_USER_LOGIN			= "xauth/user/login.do";
	public final static String	RM_USER_LOGOUT			= "xauth/user/logout.do";
	
	public final static String	RM_USER_ADD				= "xauth/user/add.do";
	public final static String	RM_USER_UPDATE			= "xauth/user/update.do";
	public final static String	RM_USER_CHPASSWD		= "xauth/user/chpasswd.do";
	public final static String	RM_USER_LOCK			= "xauth/user/lock.do";
	public final static String	RM_USER_UNLOCK			= "xauth/user/unlock.do";
	public final static String	RM_USER_REMOVE			= "xauth/user/remove.do";
	public final static String	RM_USER_DELETE			= "xauth/user/delete.do";
	
	public final static String	RM_USER_EXISTS			= "xauth/user/exists.do";
	public final static String	RM_USER_UPDATEABLE		= "xauth/user/updateable.do";
	public final static String	RM_USER_LOCKED			= "xauth/user/locked.do";
	public final static String	RM_USER_REMOVEABLE		= "xauth/user/removeable.do";
	public final static String	RM_USER_DELETEABLE		= "xauth/user/deleteable.do";
	public final static String	RM_USER_GET				= "xauth/user/get.do";
	public final static String	RM_USER_QUERY			= "xauth/user/query.do";
	public final static String	RM_USER_COUNT			= "xauth/user/count.do";
	
	public final static String	RM_USER_ROLE_ADD		= "xauth/user/role/add.do";
	public final static String	RM_USER_ROLE_LOCK		= "xauth/user/role/lock.do";
	public final static String	RM_USER_ROLE_UNLOCK		= "xauth/user/role/unlock.do";
	public final static String	RM_USER_ROLE_REMOVE		= "xauth/user/role/remove.do";
	public final static String	RM_USER_ROLE_DELETE		= "xauth/user/role/delete.do";
	
	public final static String	RM_USER_ROLE_EXISTS		= "xauth/user/role/exists.do";
	public final static String	RM_USER_ROLE_UPDATEABLE	= "xauth/user/role/updateable.do";
	public final static String	RM_USER_ROLE_LOCKED		= "xauth/user/role/locked.do";
	public final static String	RM_USER_ROLE_REMOVEABLE	= "xauth/user/role/removeable.do";
	public final static String	RM_USER_ROLE_DELETEABLE	= "xauth/user/role/deleteable.do";
	
	public final static String	RM_USER_LOGIN_QUERY		= "xauth/user/login/query.do";
	public final static String	RM_USER_LOGIN_COUNT		= "xauth/user/login/count.do";
	
	@InitBinder
	public void initBinder(HttpServletRequest request, WebDataBinder binder) {
		String uri = request.getRequestURI();
		commonValidator.setValidators(validatorMapping.getValidatorByUri(uri));
		binder.setValidator(commonValidator);
		if (logger.isDebugEnabled()) {
			logger.debug("init binder for " + uri);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_LOGIN, method = RequestMethod.POST)
	public Object login(@RequestParam(value = "authcode") String authCode, @Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		String sessionAuthCode = (String) SessionUtils.getSessionAttribute(request, AuthUtils.AUTHCODE_SESSION_KEY, true);
		
		if (authCode == null || !authCode.equalsIgnoreCase(sessionAuthCode)) {
			status = Status.CODE_NOT_MATCH;
			msg = "验证码错误";
			r = new Result(status, msg);
		} else if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			try {
				User u = userService.auth(user, RequestUtils.getRequestAddress(request), Agent.CHROME, "20.12.01");
				AuthUtils.setCurrentUser(request, u);
				status = Status.SUCCESS;
				msg = "用户" + user.getName() + "登录成功";
				r = new Result(status, msg, u);
			} catch (AuthException e) {
				status = e.getStatus();
				msg = e.getMessage();
				r = new Result(status, msg);
			}
		}
		
		logger.info("[" + status + "]" + msg);
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_LOGOUT, method = RequestMethod.GET)
	public Object logout(HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.removeCurrentUser(request);
		String uname = user.getName();
		String msg = null;
		
		short status = Status.SUCCESS;
		
		logger.info("[" + status + "]" + msg);
		
		return new Result(status, msg);
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ADD, method = RequestMethod.POST)
	public Object addUser(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setOwner(request, user);
			AuthUtils.setModifier(request, user);
			try {
				status = (short) userService.addUser(cuser, user);
				if (status == Status.SUCCESS) {
					msg = "添加用户" + user.getName() + "成功";
				} else {
					msg = "添加用户" + user.getName() + "失败";
				}
			} catch (AuthException e) {
				status = e.getStatus();
				msg = e.getMessage();
			}
			r = new Result(status, msg, user);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_UPDATE, method = RequestMethod.POST)
	public Object updateUser(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, user);
			status = (short) userService.updateUser(cuser, user);
			if (status == Status.SUCCESS) {
				msg = "更新用户[" + user.getId() + "]" + user.getName() + "成功";
				r = new Result(status, msg, user);
			} else {
				msg = "更新用户[" + user.getId() + "]" + user.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_CHPASSWD, method = RequestMethod.POST)
	public Object changePassword(@RequestParam(value = "npasswd") String newPassword, @RequestParam(value = "cpasswd") String confirmPassword, @Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		User cuser = AuthUtils.getCurrentUser(request);
		
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			Validator validator = validatorMapping.getValidatorByName(UserPasswordValidator.class.getName());
			User up = new User();
			up.setPassword(newPassword);
			BindException be = new BindException(up, "user");
			validator.validate(up, be);
			if (be.hasErrors()) {
				Result re = ValidatorUtils.errorResult(be);
				status = re.getStatus();
				msg = "新" + re.getMessage();
				re.setMessage(msg);
				r = re;
			} else {
				if (!newPassword.equals(confirmPassword)) {
					status = Status.PASSWD_NOT_MATCH;
					msg = "新密码确认错误";
				} else {
					AuthUtils.setModifier(request, user);
					cuser.setPassword(user.getPassword());
					status = (short) userService.changePassword(cuser, cuser, newPassword);
					if (status == Status.SUCCESS) {
						msg = "用户[" + cuser.getId() + "]" + cuser.getName() + "修改密码成功";
					} else {
						msg = "用户[" + cuser.getId() + "]" + cuser.getName() + "修改密码失败";
					}
				}
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_LOCK, method = RequestMethod.POST)
	public Object lockUser(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, user);
			status = (short) userService.lockUser(cuser, user);
			if (status == Status.SUCCESS) {
				msg = "锁定用户[" + user.getId() + "]" + user.getName() + "成功";
				r = new Result(status, msg, user);
			} else {
				msg = "锁定用户[" + user.getId() + "]" + user.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_UNLOCK, method = RequestMethod.POST)
	public Object unlockUser(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, user);
			status = (short) userService.unlockUser(cuser, user);
			if (status == Status.SUCCESS) {
				msg = "解锁用户[" + user.getId() + "]" + user.getName() + "成功";
				r = new Result(status, msg, user);
			} else {
				msg = "解锁用户[" + user.getId() + "]" + user.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_REMOVE, method = RequestMethod.POST)
	public Object removeUser(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, user);
			status = (short) userService.removeUser(cuser, user);
			if (status == Status.SUCCESS) {
				msg = "移除用户[" + user.getId() + "]" + user.getName() + "成功";
				r = new Result(status, msg, user);
			} else {
				msg = "移除用户[" + user.getId() + "]" + user.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_DELETE, method = RequestMethod.POST)
	public Object deleteUser(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			status = (short) userService.deleteUser(cuser, user);
			if (status == Status.SUCCESS) {
				msg = "删除用户[" + user.getId() + "]" + user.getName() + "成功";
				r = new Result(status, msg, user);
			} else {
				msg = "删除用户[" + user.getId() + "]" + user.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_EXISTS, method = RequestMethod.GET)
	public Object isUserExists(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = userService.isUserExists(cuser, user);
			if (is) {
				msg = "查询用户" + user.getName() + "存在";
				r = new Result(status, msg, true);
			} else {
				msg = "查询用户" + user.getName() + "不存在";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_UPDATEABLE, method = RequestMethod.GET)
	public Object isUserUpdateable(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = userService.isUserUpdateable(cuser, user);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_LOCKED, method = RequestMethod.GET)
	public Object isUserLocked(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = userService.isUserLocked(cuser, user);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_REMOVEABLE, method = RequestMethod.GET)
	public Object isUserRemoveable(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = userService.isUserRemoveable(cuser, user);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_DELETEABLE, method = RequestMethod.GET)
	public Object isUserDeleteable(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = userService.isUserDeleteable(cuser, user);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_GET, method = RequestMethod.GET)
	public Object getUser(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name, HttpServletRequest request) {
		User cuser = AuthUtils.getCurrentUser(request);
		String uname = cuser.getName();
		User user = null;
		if (id != null) {
			logger.info("用户" + uname + "按ID[" + id + "]查询用户");
			user = userService.getUser(cuser, id);
		} else if (StringUtils.isNotEmpty(name)) {
			logger.info("用户" + uname + "按名称[" + name + "]查询用户");
			user = userService.getUser(cuser, name);
		}
		
		String msg = null;
		short status = Status.SUCCESS;
		
		if (user != null) {
			msg = "用户[" + user.getId() + "]" + user.getName() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "用户查询失败";
		}
		Result r = new Result(status, msg, user);
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_QUERY, method = RequestMethod.GET)
	public Object getUsers(QueryBase query, HttpServletRequest request) {
		User cuser = AuthUtils.getCurrentUser(request);
		String uname = cuser.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "查询用户信息");
			} else {
				query = new QueryBase();
				logger.info("用户" + uname + "查询用户信息");
			}
		}
		userService.getUsers(cuser, query);
		
		String msg = "条件查询用户信息";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_COUNT, method = RequestMethod.GET)
	public Object countUsers(QueryBase query, HttpServletRequest request) {
		User cuser = AuthUtils.getCurrentUser(request);
		String uname = cuser.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "统计用户信息");
			} else {
				logger.info("用户" + uname + "统计用户信息");
			}
		}
		long count = userService.countUsers(cuser, query);
		
		String msg = "条件统计用户信息，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_ADD, method = RequestMethod.POST)
	public Object addUserRole(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setOwner(request, user);
			AuthUtils.setModifier(request, user);
			try {
				status = (short) userService.addUserRole(cuser, user, user.getRoles());
				if (status == Status.SUCCESS) {
					msg = "添加用户" + user.getName() + "角色成功";
				} else {
					msg = "添加用户" + user.getName() + "角色失败";
				}
			} catch (AuthException e) {
				status = e.getStatus();
				msg = e.getMessage();
			}
			r = new Result(status, msg, user);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_LOCK, method = RequestMethod.POST)
	public Object lockUserRole(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, user);
			status = (short) userService.lockUserRole(cuser, user, user.getRoles());
			if (status == Status.SUCCESS) {
				msg = "锁定用户[" + user.getId() + "]" + user.getName() + "角色成功";
				r = new Result(status, msg, user);
			} else {
				msg = "锁定用户[" + user.getId() + "]" + user.getName() + "角色失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_UNLOCK, method = RequestMethod.POST)
	public Object unlockUserRole(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, user);
			status = (short) userService.unlockUserRole(cuser, user, user.getRoles());
			if (status == Status.SUCCESS) {
				msg = "解锁用户[" + user.getId() + "]" + user.getName() + "角色成功";
				r = new Result(status, msg, user);
			} else {
				msg = "解锁用户[" + user.getId() + "]" + user.getName() + "角色失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_REMOVE, method = RequestMethod.POST)
	public Object removeUserRole(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, user);
			status = (short) userService.removeUserRole(cuser, user, user.getRoles());
			if (status == Status.SUCCESS) {
				msg = "移除用户[" + user.getId() + "]" + user.getName() + "角色成功";
				r = new Result(status, msg, user);
			} else {
				msg = "移除用户[" + user.getId() + "]" + user.getName() + "角色失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_DELETE, method = RequestMethod.POST)
	public Object deleteUserRole(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			status = (short) userService.deleteUserRole(cuser, user, user.getRoles());
			if (status == Status.SUCCESS) {
				msg = "删除用户[" + user.getId() + "]" + user.getName() + "角色成功";
				r = new Result(status, msg, user);
			} else {
				msg = "删除用户[" + user.getId() + "]" + user.getName() + "角色失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_EXISTS, method = RequestMethod.GET)
	public Object isUserRoleExists(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Role> roles = user.getRoles();
			Role role = roles.get(0);
			boolean is = userService.hasUserRole(cuser, user, role);
			if (is) {
				msg = "查询用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "存在";
				r = new Result(status, msg, true);
			} else {
				msg = "查询用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "不存在";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_UPDATEABLE, method = RequestMethod.GET)
	public Object isUserRoleUpdateable(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Role> roles = user.getRoles();
			Role role = roles.get(0);
			boolean is = userService.isUserRoleUpdateable(cuser, user, role);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_LOCKED, method = RequestMethod.GET)
	public Object isUserRoleLocked(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Role> roles = user.getRoles();
			Role role = roles.get(0);
			boolean is = userService.isUserRoleLocked(cuser, user, role);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_REMOVEABLE, method = RequestMethod.GET)
	public Object isUserRoleRemoveable(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Role> roles = user.getRoles();
			Role role = roles.get(0);
			boolean is = userService.isUserRoleRemoveable(cuser, user, role);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_USER_ROLE_DELETEABLE, method = RequestMethod.GET)
	public Object isUserRoleDeleteable(@Validated User user, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Role> roles = user.getRoles();
			Role role = roles.get(0);
			boolean is = userService.isUserRoleDeleteable(cuser, user, role);
			if (is) {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到用户[" + user.getId() + "]" + user.getName() + "角色[" + role.getId() + "]" + role.getName() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + cuser.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_USER_AUTH_CODE, method = RequestMethod.GET)
	public void authCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			String authCode = ResponseUtils.responseAuthCode(request, response);
			SessionUtils.setSessionAttribute(request, AuthUtils.AUTHCODE_SESSION_KEY, authCode);
			logger.info("生成随机验证码" + authCode);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public UserService getUserService() {
		return userService;
	}

}