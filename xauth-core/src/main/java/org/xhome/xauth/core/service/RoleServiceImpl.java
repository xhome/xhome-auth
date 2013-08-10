package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.dao.RoleDAO;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:49 PM
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleDAO	roleDAO;
	@Autowired
	private ManageLogService manageLogService;
	
	private Logger	logger;
	
	public RoleServiceImpl() {
		logger = LoggerFactory.getLogger(RoleService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addRole(User oper, Role role) {
		String name = role.getName();
		if (roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add role {}, but it's already exists", name);
			}
			
			this.logManage(name, Action.ADD, null, Status.EXISTS, oper);
			return Status.EXISTS;
		}
		
		role.setStatus(Status.OK);
		role.setVersion((short)0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		role.setCreated(t);
		role.setModified(t);
		
		short r = roleDAO.addRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add role {}", name);
			} else {
				logger.debug("fail to add role {}", name);
			}
		}

		this.logManage(name, Action.ADD, role.getId(), r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateRole(User oper, Role role) {
		Long id = role.getId();
		Role old = roleDAO.queryRoleById(id);
		String name = role.getName();
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}], but it's not exists", name, id);
			}
			
			this.logManage(name, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(role.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}], but version not match", oldName, id);
			}
			
			this.logManage(oldName, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update role {}[{}]", oldName, id);
			}
			
			this.logManage(oldName, Action.UPDATE, id, status, oper);
			return status;
		}
		
		if (!oldName.equals(name) && roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}] to {}, but it's exists", oldName, id, name);
			}
			this.logManage(oldName, Action.UPDATE, id, Status.EXISTS, oper);
			return Status.EXISTS;
		}
		
		role.setOwner(old.getOwner());
		role.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		role.setModified(t);
		
		int r  = roleDAO.updateRole(role);
		if (r== 1) {
			role.incrementVersion();
			r = Status.SUCCESS;
		} else {
			r = Status.ERROR;
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update role {}[{}]", oldName, id);
			} else {
				logger.debug("fail to update role {}[{}]", oldName, id);
			}
		}

		this.logManage(oldName, Action.UPDATE, id, (short) r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockRole(User oper, Role role) {
		short r = roleDAO.lockRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		String name = role.getName();
		Long id = role.getId();
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock role {}[{}]", name, id);
			} else {
				logger.debug("fail to lock role {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.LOCK, id, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockRole(User oper, Role role) {
		short r = roleDAO.unlockRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		String name = role.getName();
		Long id = role.getId();
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock role {}[{}]", name, id);
			} else {
				logger.debug("fail to unlock role {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.UNLOCK, id, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeRole(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		short r = Status.SUCCESS;
		if (roleDAO.isRoleRemoveable(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove role {}[{}]", name, id);
			}
			roleDAO.removeRole(role);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("role {}[{}] isn't removeable", name, id);
			}
			r = Status.NO_REMOVE;
		}
		
		this.logManage(name, Action.REMOVE, id, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteRole(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		short r = Status.SUCCESS;
		if (roleDAO.isRoleDeleteable(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete role {}[{}]", role.getName(), role.getId());
			}
			roleDAO.deleteRole(role);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("role {}[{}] isn't deleteable", role.getName(), role.getId());
			}
			r = Status.NO_DELETE;
		}

		this.logManage(name, Action.DELETE, id, r, oper);
		return r;
	}
	
	@Override
	public boolean isRoleExists(User oper, Role role) {
		boolean e = roleDAO.isRoleExists(role);
		
		String name = role.getName();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of role {}", name);
			} else {
				logger.debug("not exists of role {}", name);
			}
		}
		
		this.logManage(name, Action.IS_EXISTS, role.getId(), Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isRoleUpdateable(User oper, Role role) {
		boolean e = roleDAO.isRoleUpdateable(role);

		String name = role.getName();
		Long id = role.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is updateable", name, id);
			} else {
				logger.debug("role {}[{}] isn't updateable", name, id);
			}
		}
		
		this.logManage(name, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isRoleLocked(User oper, Role role) {
		boolean e = roleDAO.isRoleLocked(role);
		
		String name = role.getName();
		Long id = role.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is locked", name, id);
			} else {
				logger.debug("role {}[{}] isn't locked", name, id);
			}
		}

		this.logManage(name, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isRoleRemoveable(User oper, Role role) {
		boolean e = roleDAO.isRoleRemoveable(role);
		
		String name = role.getName();
		Long id = role.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is removeable", name, id);
			} else {
				logger.debug("role {}[{}] isn't removeable", name, id);
			}
		}
		
		this.logManage(name, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isRoleDeleteable(User oper, Role role) {
		boolean e = roleDAO.isRoleDeleteable(role);

		String name = role.getName();
		Long id = role.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is deleteable", name, id);
			} else {
				logger.debug("role {}[{}] isn't deleteable", name, id);
			}
		}

		this.logManage(name, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public Role getRole(User oper, long id) {
		Role role = roleDAO.queryRoleById(id);
		
		String name = null;
		if (logger.isDebugEnabled()) {
			if (role != null) {
				name = role.getName();
				logger.debug("get role {}[{}]", name, id);
			} else {
				logger.debug("role of id {} is not exists", id);
			}
		}
		
		this.logManage(name, Action.GET, id, Status.SUCCESS, oper);
		return role;
	}
	
	@Override
	public Role getRole(User oper, String name) {
		Role role = roleDAO.queryRoleByName(name);
		
		Long id = null;
		if (logger.isDebugEnabled()) {
			if (role != null) {
				id = role.getId();
				logger.debug("get role {}[{}]", name, id);
			} else {
				logger.debug("role {} is not exists", name);
			}
		}

		this.logManage(name, Action.GET, id, Status.SUCCESS, oper);
		return role;
	}
	
	@Override
	public List<Role> getRoles(User oper) {
		return getRoles(oper, null);
	}
	
	@Override
	public List<Role> getRoles(User oper, QueryBase query) {
		List<Role> results = roleDAO.queryRoles(query);
		if (query != null) {
			query.setResults(results);
			long total = roleDAO.countRoles(query);
			query.setTotalRow(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query roles with parameters {}", query.getParameters());
			} else {
				logger.debug("query roles");
			}
		}
		
		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		return results;
	}
	
	@Override
	public long countRoles(User oper) {
		return countRoles(oper, null);
	}
	
	@Override
	public long countRoles(User oper, QueryBase query) {
		long c = roleDAO.countRoles(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count roles with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count roles of {}", c);
			}
		}
		
		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLog.TYPE_ROLE, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
}
