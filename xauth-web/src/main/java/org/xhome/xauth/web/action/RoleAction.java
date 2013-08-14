package org.xhome.xauth.web.action;

import javax.servlet.http.HttpServletRequest;

import org.jhat.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.http.response.Result;
import org.xhome.util.StringUtils;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.RoleService;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xauth.web.util.ValidatorUtils;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description 
 */
@Controller
public class RoleAction {

	@Autowired(required = false)
	private RoleService roleService;
	private Logger logger = LoggerFactory.getLogger(RoleAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_ROLE_ADD			= "xauth/role/add.do";
	public final static String	RM_ROLE_UPDATE		= "xauth/role/update.do";
	public final static String	RM_ROLE_LOCK		= "xauth/role/lock.do";
	public final static String	RM_ROLE_UNLOCK		= "xauth/role/unlock.do";
	public final static String	RM_ROLE_REMOVE		= "xauth/role/remove.do";
	public final static String	RM_ROLE_DELETE		= "xauth/role/delete.do";
	
	public final static String	RM_ROLE_EXISTS		= "xauth/role/exists.do";
	public final static String	RM_ROLE_UPDATEABLE	= "xauth/role/updateable.do";
	public final static String	RM_ROLE_LOCKED		= "xauth/role/locked.do";
	public final static String	RM_ROLE_REMOVEABLE	= "xauth/role/removeable.do";
	public final static String	RM_ROLE_DELETEABLE	= "xauth/role/deleteable.do";
	public final static String	RM_ROLE_GET			= "xauth/role/get.do";
	public final static String	RM_ROLE_QUERY		= "xauth/role/query.do";
	public final static String	RM_ROLE_COUNT		= "xauth/role/count.do";
	
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
	@RequestMapping(value = RM_ROLE_ADD, method = RequestMethod.POST)
	public Object addRole(@Validated @RequestAttribute("role") Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setOwner(request, role);
			AuthUtils.setModifier(request, role);
			status = (short) roleService.addRole(user, role);
			if (status == Status.SUCCESS) {
				msg = "添加角色" + role.getName() + "成功";
				r = new Result(status, msg, role);
			} else {
				msg = "添加角色" + role.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_UPDATE, method = RequestMethod.POST)
	public Object updateRole(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, role);
			status = (short) roleService.updateRole(user, role);
			if (status == Status.SUCCESS) {
				msg = "更新角色[" + role.getId() + "]" + role.getName() + "成功";
				r = new Result(status, msg, role);
			} else {
				msg = "更新角色[" + role.getId() + "]" + role.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_LOCK, method = RequestMethod.POST)
	public Object lockRole(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, role);
			status = (short) roleService.lockRole(user, role);
			if (status == Status.SUCCESS) {
				msg = "锁定角色[" + role.getId() + "]" + role.getName() + "成功";
				r = new Result(status, msg, role);
			} else {
				msg = "锁定角色[" + role.getId() + "]" + role.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_UNLOCK, method = RequestMethod.POST)
	public Object unlockRole(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, role);
			status = (short) roleService.unlockRole(user, role);
			if (status == Status.SUCCESS) {
				msg = "解锁角色[" + role.getId() + "]" + role.getName() + "成功";
				r = new Result(status, msg, role);
			} else {
				msg = "解锁角色[" + role.getId() + "]" + role.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_REMOVE, method = RequestMethod.POST)
	public Object removeRole(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, role);
			status = (short) roleService.removeRole(user, role);
			if (status == Status.SUCCESS) {
				msg = "移除角色[" + role.getId() + "]" + role.getName() + "成功";
				r = new Result(status, msg, role);
			} else {
				msg = "移除角色[" + role.getId() + "]" + role.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_DELETE, method = RequestMethod.POST)
	public Object deleteRole(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			status = (short) roleService.deleteRole(user, role);
			if (status == Status.SUCCESS) {
				msg = "删除角色[" + role.getId() + "]" + role.getName() + "成功";
				r = new Result(status, msg, role);
			} else {
				msg = "删除角色[" + role.getId() + "]" + role.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_EXISTS, method = RequestMethod.GET)
	public Object isRoleExists(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = roleService.isRoleExists(user, role);
			if (is) {
				msg = "查询角色" + role.getName() + "存在";
				r = new Result(status, msg, true);
			} else {
				msg = "查询角色" + role.getName() + "不存在";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_UPDATEABLE, method = RequestMethod.GET)
	public Object isRoleUpdateable(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = roleService.isRoleUpdateable(user, role);
			if (is) {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_LOCKED, method = RequestMethod.GET)
	public Object isRoleLocked(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = roleService.isRoleLocked(user, role);
			if (is) {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_REMOVEABLE, method = RequestMethod.GET)
	public Object isRoleRemoveable(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = roleService.isRoleRemoveable(user, role);
			if (is) {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_DELETEABLE, method = RequestMethod.GET)
	public Object isRoleDeleteable(@Validated Role role, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = roleService.isRoleDeleteable(user, role);
			if (is) {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到角色[" + role.getId() + "]" + role.getName() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_GET, method = RequestMethod.GET)
	public Object getRole(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		Role role = null;
		if (id != null) {
			logger.info("用户" + uname + "按ID[" + id + "]查询角色");
			role = roleService.getRole(user, id);
		} else if (StringUtils.isNotEmpty(name)) {
			logger.info("用户" + uname + "按名称[" + name + "]查询角色");
			role = roleService.getRole(user, name);
		}
		
		String msg = null;
		short status = Status.SUCCESS;
		
		if (role != null) {
			msg = "角色[" + role.getId() + "]" + role.getName() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "角色查询失败";
		}
		Result r = new Result(status, msg, role);
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_QUERY, method = RequestMethod.GET)
	public Object getRoles(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "查询角色信息");
			} else {
				query = new QueryBase();
				logger.info("用户" + uname + "查询角色信息");
			}
		}
		roleService.getRoles(user, query);
		
		String msg = "条件查询角色信息";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ROLE_COUNT, method = RequestMethod.GET)
	public Object countRoles(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "统计角色信息");
			} else {
				logger.info("用户" + uname + "统计角色信息");
			}
		}
		long count = roleService.countRoles(user, query);
		
		String msg = "条件统计角色信息，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public RoleService getRoleService() {
		return roleService;
	}
	
}
