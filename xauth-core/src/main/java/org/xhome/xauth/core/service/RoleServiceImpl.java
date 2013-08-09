package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Status;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xauth.core.dao.RoleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Logger	logger;
	
	public RoleServiceImpl() {
		logger = LoggerFactory.getLogger(RoleService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addRole(Role role) {
		String name = role.getName();
		if (roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add role {}, but it's already exists", name);
			}
			return Status.EXISTS;
		}
		
		role.setStatus(Status.OK);
		role.setVersion((short)0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		role.setCreated(t);
		role.setModified(t);
		
		int r = roleDAO.addRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add role {}", name);
			} else {
				logger.debug("fail to add role {}", name);
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateRole(Role role) {
		Long id = role.getId();
		Role old = roleDAO.queryRoleById(id);
		String name = role.getName();
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}], but it's not exists", name, id);
			}
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(role.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}], but version not match", oldName, id);
			}
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update role {}[{}]", oldName, id);
			}
			return status;
		}
		
		if (!oldName.equals(name) && roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update role {}[{}] to {}, but it's exists", oldName, id, name);
			}
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
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockRole(Role role) {
		int r = roleDAO.lockRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock role {}[{}]", role.getName(), role.getId());
			} else {
				logger.debug("fail to lock role {}[{}]", role.getName(), role.getId());
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockRole(Role role) {
		int r = roleDAO.unlockRole(role) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock role {}[{}]", role.getName(), role.getId());
			} else {
				logger.debug("fail to unlock role {}[{}]", role.getName(), role.getId());
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeRole(Role role) {
		if (roleDAO.isRoleRemoveable(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove role {}[{}]", role.getName(), role.getId());
			}
			roleDAO.removeRole(role);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("role {}[{}] isn't removeable", role.getName(), role.getId());
			}
		}
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteRole(Role role) {
		if (roleDAO.isRoleDeleteable(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete role {}[{}]", role.getName(), role.getId());
			}
			roleDAO.deleteRole(role);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("role {}[{}] isn't deleteable", role.getName(), role.getId());
			}
		}
		return Status.SUCCESS;
	}
	
	@Override
	public boolean isRoleExists(Role role) {
		boolean e = roleDAO.isRoleExists(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of role {}", role.getName());
			} else {
				logger.debug("not exists of role {}", role.getName());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isRoleUpdateable(Role role) {
		boolean e = roleDAO.isRoleUpdateable(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is updateable", role.getName(), role.getId());
			} else {
				logger.debug("role {}[{}] isn't updateable", role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isRoleLocked(Role role) {
		boolean e = roleDAO.isRoleLocked(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is locked", role.getName(), role.getId());
			} else {
				logger.debug("role {}[{}] isn't locked", role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isRoleRemoveable(Role role) {
		boolean e = roleDAO.isRoleRemoveable(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is removeable", role.getName(), role.getId());
			} else {
				logger.debug("role {}[{}] isn't removeable", role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isRoleDeleteable(Role role) {
		boolean e = roleDAO.isRoleDeleteable(role);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("role {}[{}] is deleteable", role.getName(), role.getId());
			} else {
				logger.debug("role {}[{}] isn't deleteable", role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public Role getRole(long id) {
		Role role = roleDAO.queryRoleById(id);
		
		if (logger.isDebugEnabled()) {
			if (role != null) {
				logger.debug("get role {}[{}]", role.getName(), role.getId());
			} else {
				logger.debug("role of id {} is not exists", id);
			}
		}
		
		return role;
	}
	
	@Override
	public Role getRole(String name) {
		Role role = roleDAO.queryRoleByName(name);
		
		if (logger.isDebugEnabled()) {
			if (role != null) {
				logger.debug("get role {}[{}]", name, role.getId());
			} else {
				logger.debug("role {} is not exists", name);
			}
		}
		
		return role;
	}
	
	@Override
	public List<Role> getRoles() {
		return getRoles(null);
	}
	
	@Override
	public List<Role> getRoles(QueryBase query) {
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
		
		return results;
	}
	
	@Override
	public long countRoles() {
		return countRoles(null);
	}
	
	@Override
	public long countRoles(QueryBase query) {
		long c = roleDAO.countRoles(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count roles with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count roles of {}", c);
			}
		}
		return c;
	}
	
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
}
