package org.xhome.xauth.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.xhome.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.util.StringUtils;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.response.DataResult;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.RoleService;
import org.xhome.xauth.web.util.AuthUtils;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description 
 */
@Controller
public class RoleAction extends AbstractAction {

	@Autowired(required = false)
	private RoleService roleService;
	
	public final static String	RM_ROLE_ADD			= "xauth/role/add";
	public final static String	RM_ROLE_UPDATE		= "xauth/role/update";
	public final static String	RM_ROLE_LOCK		= "xauth/role/lock";
	public final static String	RM_ROLE_UNLOCK		= "xauth/role/unlock";
	public final static String	RM_ROLE_REMOVE		= "xauth/role/remove";
	public final static String	RM_ROLE_DELETE		= "xauth/role/delete";
	
	public final static String	RM_ROLE_EXISTS		= "xauth/role/exists";
	public final static String	RM_ROLE_UPDATEABLE	= "xauth/role/updateable";
	public final static String	RM_ROLE_LOCKED		= "xauth/role/locked";
	public final static String	RM_ROLE_REMOVEABLE	= "xauth/role/removeable";
	public final static String	RM_ROLE_DELETEABLE	= "xauth/role/deleteable";
	public final static String	RM_ROLE_GET			= "xauth/role/get";
	public final static String	RM_ROLE_QUERY		= "xauth/role/query";
	public final static String	RM_ROLE_COUNT		= "xauth/role/count";
	
	@RequestMapping(value = RM_ROLE_ADD, method = RequestMethod.POST)
	public Object addRole(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, role);
		AuthUtils.setModifier(request, role);
		status = (short) roleService.addRole(user, role);
		if (status == Status.SUCCESS) {
			msg = "添加角色" + role.getName() + "成功";
			r = new CommonResult(status, msg, role);
		} else {
			msg = "添加角色" + role.getName() + "失败";
			r = new CommonResult(status, msg);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_UPDATE, method = RequestMethod.POST)
	public Object updateRole(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, role);
		status = (short) roleService.updateRole(user, role);
		if (status == Status.SUCCESS) {
			msg = "更新角色[" + role.getId() + "]" + role.getName() + "成功";
			r = new CommonResult(status, msg, role);
		} else {
			msg = "更新角色[" + role.getId() + "]" + role.getName() + "失败";
			r = new CommonResult(status, msg);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_LOCK, method = RequestMethod.POST)
	public Object lockRole(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, role);
		status = (short) roleService.lockRole(user, role);
		if (status == Status.SUCCESS) {
			msg = "锁定角色[" + role.getId() + "]" + role.getName() + "成功";
			r = new CommonResult(status, msg, role);
		} else {
			msg = "锁定角色[" + role.getId() + "]" + role.getName() + "失败";
			r = new CommonResult(status, msg);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_UNLOCK, method = RequestMethod.POST)
	public Object unlockRole(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, role);
		status = (short) roleService.unlockRole(user, role);
		if (status == Status.SUCCESS) {
			msg = "解锁角色[" + role.getId() + "]" + role.getName() + "成功";
			r = new CommonResult(status, msg, role);
		} else {
			msg = "解锁角色[" + role.getId() + "]" + role.getName() + "失败";
			r = new CommonResult(status, msg);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_REMOVE, method = RequestMethod.POST)
	public Object removeRole(@Validated @RequestAttribute("roles") List<Role> roles, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		for (Role role : roles) {
			AuthUtils.setModifier(request, role);
		}
		try {
			status = (short) roleService.removeRoles(user, roles);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "角色移除成功";
			r = new CommonResult(status, msg, roles);
		} else {
			msg = "角色移除失败";
			r = new CommonResult(status, msg);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_DELETE, method = RequestMethod.POST)
	public Object deleteRole(@Validated @RequestAttribute("roles") List<Role> roles, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		for (Role role : roles) {
			AuthUtils.setModifier(request, role);
		}
		try {
			status = (short) roleService.deleteRoles(user, roles);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "角色删除成功";
			r = new CommonResult(status, msg, roles);
		} else {
			msg = "角色删除失败";
			r = new CommonResult(status, msg);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_EXISTS, method = RequestMethod.GET)
	public Object isRoleExists(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		boolean is = roleService.isRoleExists(user, role);
		if (is) {
			msg = "查询角色" + role.getName() + "存在";
			r = new CommonResult(status, msg, true);
		} else {
			msg = "查询角色" + role.getName() + "不存在";
			r = new CommonResult(status, msg, false);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_UPDATEABLE, method = RequestMethod.GET)
	public Object isRoleUpdateable(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		boolean is = roleService.isRoleUpdateable(user, role);
		if (is) {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "可以更新";
			r = new CommonResult(status, msg, true);
		} else {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "不可以更新";
			r = new CommonResult(status, msg, false);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_LOCKED, method = RequestMethod.GET)
	public Object isRoleLocked(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		boolean is = roleService.isRoleLocked(user, role);
		if (is) {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "已被锁定";
			r = new CommonResult(status, msg, true);
		} else {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "未被锁定";
			r = new CommonResult(status, msg, false);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_REMOVEABLE, method = RequestMethod.GET)
	public Object isRoleRemoveable(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		boolean is = roleService.isRoleRemoveable(user, role);
		if (is) {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "可以移除";
			r = new CommonResult(status, msg, true);
		} else {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "不可以移除";
			r = new CommonResult(status, msg, false);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@RequestMapping(value = RM_ROLE_DELETEABLE, method = RequestMethod.GET)
	public Object isRoleDeleteable(@Validated @RequestAttribute("role") Role role, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		boolean is = roleService.isRoleDeleteable(user, role);
		if (is) {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "可以删除";
			r = new CommonResult(status, msg, true);
		} else {
			msg = "查询到角色[" + role.getId() + "]" + role.getName() + "不可以删除";
			r = new CommonResult(status, msg, false);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
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
		CommonResult r = new CommonResult(status, msg, role);
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
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
		
		DataResult r = new DataResult(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
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
		
		CommonResult r = new CommonResult(status, msg, count);

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
