package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.dao.RoleDAO;
import org.xhome.xauth.core.listener.RoleManageListener;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:49 PM
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired(required = false)
	private RoleDAO	roleDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<RoleManageListener> roleManageListeners;
	
	private Logger	logger;
	
	public RoleServiceImpl() {
		logger = LoggerFactory.getLogger(RoleService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addRole(User oper, Role role) {
		String name = role.getName();
		
		if (!this.beforeRoleManage(oper, Action.ADD, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add role {}, but it's blocked", name);
			}
			
			this.logManage(name, Action.ADD, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.ADD, Status.BLOCKED, role);
			return Status.BLOCKED;
		}
		
		if (roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add role {}, but it's already exists", name);
			}
			
			this.logManage(name, Action.ADD, null, Status.EXISTS, oper);
			this.afterRoleManage(oper, Action.ADD, Status.EXISTS, role);
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
		this.afterRoleManage(oper, Action.ADD, r, role);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateRole(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.UPDATE, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.UPDATE, Status.BLOCKED, role);
			return Status.BLOCKED;
		}
		
		Role old = roleDAO.queryRoleById(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}], but it's not exists", name, id);
			}
			
			this.logManage(name, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterRoleManage(oper, Action.UPDATE, Status.NOT_EXISTS, role);
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(role.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}], but version not match", oldName, id);
			}
			
			this.logManage(oldName, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterRoleManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, role);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update role {}[{}]", oldName, id);
			}
			
			this.logManage(oldName, Action.UPDATE, id, status, oper);
			this.afterRoleManage(oper, Action.UPDATE, Status.EXISTS, role);
			return status;
		}
		
		if (!oldName.equals(name) && roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}] to {}, but it's exists", oldName, id, name);
			}
			this.logManage(oldName, Action.UPDATE, id, Status.EXISTS, oper);
			this.afterRoleManage(oper, Action.UPDATE, Status.EXISTS, role);
			return Status.EXISTS;
		}
		
		role.setOwner(old.getOwner());
		role.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		role.setModified(t);
		
		short r  = roleDAO.updateRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		if (r == Status.SUCCESS) {
			role.incrementVersion();
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update role {}[{}]", oldName, id);
			} else {
				logger.debug("fail to update role {}[{}]", oldName, id);
			}
		}

		this.logManage(oldName, Action.UPDATE, id, r, oper);
		this.afterRoleManage(oper, Action.UPDATE, r, role);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockRole(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.LOCK, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.LOCK, Status.BLOCKED, role);
			return Status.BLOCKED;
		}
		
		short r = roleDAO.lockRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock role {}[{}]", name, id);
			} else {
				logger.debug("fail to lock role {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.LOCK, id, r, oper);
		this.afterRoleManage(oper, Action.LOCK, r, role);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockRole(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.UNLOCK, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.UNLOCK, Status.BLOCKED, role);
			return Status.BLOCKED;
		}
		
		short r = roleDAO.unlockRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock role {}[{}]", name, id);
			} else {
				logger.debug("fail to unlock role {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.UNLOCK, id, r, oper);
		this.afterRoleManage(oper, Action.UNLOCK, r, role);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeRole(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.REMOVE, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.REMOVE, Status.BLOCKED, role);
			return Status.BLOCKED;
		}
		
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
		this.afterRoleManage(oper, Action.REMOVE, r, role);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteRole(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.DELETE, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.DELETE, Status.BLOCKED, role);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (roleDAO.isRoleDeleteable(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete role {}[{}]", name, id);
			}
			roleDAO.deleteRole(role);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("role {}[{}] isn't deleteable", name, id);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(name, Action.DELETE, id, r, oper);
		this.afterRoleManage(oper, Action.DELETE, r, role);
		return r;
	}
	
	@Override
	public boolean isRoleExists(User oper, Role role) {
		String name = role.getName();
		
		if (!this.beforeRoleManage(oper, Action.IS_EXISTS, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge exists of role {}, but it's blocked", name);
			}
			
			this.logManage(name, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.IS_EXISTS, Status.BLOCKED, role);
			return false;
		}
		
		boolean e = roleDAO.isRoleExists(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of role {}", name);
			} else {
				logger.debug("not exists of role {}", name);
			}
		}
		
		this.logManage(name, Action.IS_EXISTS, role.getId(), Status.SUCCESS, oper);
		this.afterRoleManage(oper, Action.IS_EXISTS, Status.SUCCESS, role);
		return e;
	}
	
	@Override
	public boolean isRoleUpdateable(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.IS_UPDATEABLE, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge updateable of role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, role);
			return false;
		}
		
		boolean e = roleDAO.isRoleUpdateable(role);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is updateable", name, id);
			} else {
				logger.debug("role {}[{}] isn't updateable", name, id);
			}
		}
		
		this.logManage(name, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterRoleManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, role);
		return e;
	}
	
	@Override
	public boolean isRoleLocked(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.IS_LOCKED, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge locked of role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.IS_LOCKED, Status.BLOCKED, role);
			return false;
		}
		
		boolean e = roleDAO.isRoleLocked(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is locked", name, id);
			} else {
				logger.debug("role {}[{}] isn't locked", name, id);
			}
		}

		this.logManage(name, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterRoleManage(oper, Action.IS_LOCKED, Status.SUCCESS, role);
		return e;
	}
	
	@Override
	public boolean isRoleRemoveable(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.IS_REMOVEABLE, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge removeable of role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, role);
			return false;
		}
		
		boolean e = roleDAO.isRoleRemoveable(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is removeable", name, id);
			} else {
				logger.debug("role {}[{}] isn't removeable", name, id);
			}
		}
		
		this.logManage(name, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterRoleManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, role);
		return e;
	}
	
	@Override
	public boolean isRoleDeleteable(User oper, Role role) {
		String name = role.getName();
		Long id = role.getId();
		
		if (!this.beforeRoleManage(oper, Action.IS_DELETEABLE, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge deleteable of role {}[{}], but it's blocked", name, id);
			}
			
			this.logManage(name, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, role);
			return false;
		}
		
		boolean e = roleDAO.isRoleDeleteable(role);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is deleteable", name, id);
			} else {
				logger.debug("role {}[{}] isn't deleteable", name, id);
			}
		}

		this.logManage(name, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterRoleManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, role);
		return e;
	}
	
	@Override
	public Role getRole(User oper, long id) {
		if (!this.beforeRoleManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get role of id {}, but it's blocked", id);
			}
			
			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}
		
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
		this.afterRoleManage(oper, Action.GET, Status.SUCCESS, role, id);
		return role;
	}
	
	@Override
	public Role getRole(User oper, String name) {
		if (!this.beforeRoleManage(oper, Action.GET, null, name)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get role {}, but it's blocked", name);
			}
			
			this.logManage(name, Action.GET, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.GET, Status.BLOCKED, null, name);
			return null;
		}
		
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
		this.afterRoleManage(oper, Action.GET, Status.SUCCESS, role, name);
		return role;
	}
	
	@Override
	public List<Role> getRoles(User oper) {
		return getRoles(oper, null);
	}
	
	@Override
	public List<Role> getRoles(User oper, QueryBase query) {
		if (!this.beforeRoleManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query roles, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
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
		this.afterRoleManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}
	
	@Override
	public long countRoles(User oper) {
		return countRoles(oper, null);
	}
	
	@Override
	public long countRoles(User oper, QueryBase query) {
		if (!this.beforeRoleManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count roles, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterRoleManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = roleDAO.countRoles(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count roles with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count roles of {}", c);
			}
		}
		
		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterRoleManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLog.TYPE_ROLE, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeRoleManage(User oper, short action, Role role, Object ...args) {
		if (roleManageListeners != null) {
			for (RoleManageListener listener : roleManageListeners) {
				if (!listener.beforeRoleManage(oper, action, role, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterRoleManage(User oper, short action, short result, Role role, Object ...args) {
		if (roleManageListeners != null) {
			for (RoleManageListener listener : roleManageListeners) {
				listener.afterRoleManage(oper, action, result, role, args);
			}
		}
	}
	
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
	public RoleDAO getRoleDAO() {
		return this.roleDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setRoleManageListeners(List<RoleManageListener> roleManageListeners) {
		this.roleManageListeners = roleManageListeners;
	}

	public List<RoleManageListener> getRoleManageListeners() {
		return roleManageListeners;
	}
	
	public void registerRoleManageListener(RoleManageListener roleManageListener) {
		if (roleManageListeners == null) {
			roleManageListeners = new ArrayList<RoleManageListener>();
		}
		roleManageListeners.add(roleManageListener);
	}
	
}
