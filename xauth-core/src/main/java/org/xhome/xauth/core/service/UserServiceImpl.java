package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthException;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.dao.RoleDAO;
import org.xhome.xauth.core.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:23 PM
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO	userDAO;
	@Autowired
	private RoleDAO	roleDAO;
	@Autowired
	private ManageLogService manageLogService;
	
	private Logger  logger;
	
	public UserServiceImpl() {
		logger = LoggerFactory.getLogger(UserService.class);
	}
	
	@Override
	public User login(User user) throws AuthException {
		String name = user.getName();
		User u = userDAO.queryUserByName(name);
		if (u == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("user {} try to login, but it's not exists", name);
			}
			throw new AuthException(Status.NOT_EXISTS, "用户" + name + "不存在");
		}
		
		short status = u.getStatus();
		if (status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("user {} try to login, but it's locked", name);
			}
			throw new AuthException(status, "用户" + name + "已锁定，不能登录");
		}
		
		String ep = this.encrypt(user.getPassword());
		if (ep.equals(u.getPassword())) {
			if (logger.isDebugEnabled()) {
				logger.debug("user {} login", name);
			}
			return u;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {} try to login, but password debug", name);
			}
			throw new AuthException(Status.PASSWD_NOT_MATCH, "密码错误");
		}
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addUser(User oper, User user)  throws AuthException {
		String name = user.getName();
		if (userDAO.isUserExists(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add user {}, but it's already exists", name);
			}
			
			this.logManageUser(name, Action.ADD, null, Status.EXISTS, oper);
			return Status.EXISTS;
		}
		
		user.setStatus(Status.OK);
		user.setVersion((short)0);
		user.setPassword(this.encrypt(user.getPassword()));
		Timestamp t = new Timestamp(System.currentTimeMillis());
		user.setCreated(t);
		user.setModified(t);
		
		int r = userDAO.addUser(user);
		
		if (r == 1) {
			r = this.addUserRole(oper, user, user.getRoles());
		} else {
			r = Status.ERROR;
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add user {}", name);
			} else {
				logger.debug("fail to add user {}", name);
			}
		}
		
		this.logManageUser(name, Action.ADD, user.getId(), (short) r, oper);
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateUser(User oper, User user) {
		Long id = user.getId();
		User old = userDAO.queryUserById(id);
		String name = user.getName();
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update user {}[{}], but it's not exists", name, id);
			}
			
			this.logManageUser(name, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(user.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update user {}[{}], but version not match", oldName, id);
			}
			
			this.logManageUser(oldName, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update user {}[{}]", oldName, id);
			}
			
			this.logManageUser(oldName, Action.UPDATE, id, status, oper);
			return status;
		}
		
		user.setOwner(old.getOwner());
		user.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		user.setModified(t);
		
		int r = userDAO.updateUser(user);
		
		if (r == 1) {
			user.incrementVersion();
			r = Status.SUCCESS;
		} else {
			r = Status.ERROR;
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update user {}[{}]", oldName, id);
			} else {
				logger.debug("fail to update user {}[{}]", oldName, id);
			}
		}
		
		this.logManageUser(oldName, Action.UPDATE, id, (short) r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int changePassword(User oper, User user, String newPassword) {
		Long id = user.getId();
		User old = userDAO.queryUserById(id);
		String name = user.getName();
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but it's not exists", name, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, Status.NOT_EXISTS, oper);
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(user.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but version not match", oldName, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update user {}[{}]", oldName, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, status, oper);
			return status;
		}
		
		String oldPassword = this.encrypt(user.getPassword());
		if (!oldPassword.equals(old.getPassword())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but old password not match", oldName, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, Status.PASSWD_NOT_MATCH, oper);
			return Status.PASSWD_NOT_MATCH;
		}
		
		newPassword = this.encrypt(newPassword);
		user.setPassword(newPassword);
		old.setPassword(newPassword);
		user.setOwner(old.getOwner());
		user.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		user.setModified(t);
		old.setModified(t);
		
		int r = userDAO.updateUser(old);
		
		if (r == 1) {
			user.incrementVersion();
			r = Status.SUCCESS;
		} else {
			r = Status.ERROR;
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to change password for user {}[{}]", oldName, id);
			} else {
				logger.debug("fail to change password for user {}[{}]", oldName, id);
			}
		}
		
		this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, (short) r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockUser(User oper, User user) {
		short r = userDAO.lockUser(user) == 1 ? Status.SUCCESS : Status.ERROR;
		
		String name = user.getName();
		Long id = user.getId();
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock user {}[{}]", name, id);
			} else {
				logger.debug("fail to lock user {}[{}]", name, id);
			}
		}
		
		this.logManageUser(name, Action.LOCK, id, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockUser(User oper, User user) {
		short r = userDAO.unlockUser(user) == 1 ? Status.SUCCESS : Status.ERROR;

		String name = user.getName();
		Long id = user.getId();
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock user {}[{}]", name, id);
			} else {
				logger.debug("fail to unlock user {}[{}]", name, id);
			}
		}

		this.logManageUser(name, Action.UNLOCK, id, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeUser(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		short r = Status.SUCCESS;
		if (userDAO.isUserDeleteable(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove user {}[{}]", name, id);
			}
			userDAO.removeUser(user);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}] isn't removeable", name, id);
			}
			r = Status.NO_REMOVE;
		}
		
		this.logManageUser(name, Action.REMOVE, id, r, oper);
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteUser(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		short r = Status.SUCCESS;
		if (userDAO.isUserDeleteable(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete user {}[{}]", name, id);
			}
			userDAO.deleteUser(user);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}] isn't deleteable", name, id);
			}
			r = Status.NO_DELETE;
		}

		this.logManageUser(name, Action.DELETE, id, r, oper);
		return Status.SUCCESS;
	}
	
	@Override
	public boolean isUserExists(User oper, User user) {
		boolean e = userDAO.isUserExists(user);
		
		String name = user.getName();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of user {}", name);
			} else {
				logger.debug("not exists of user {}", name);
			}
		}

		this.logManageUser(name, Action.IS_EXISTS, user.getId(), Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isUserUpdateable(User oper, User user) {
		boolean e = userDAO.isUserUpdateable(user);
		
		String name = user.getName();
		Long id = user.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is updateable", name, id);
			} else {
				logger.debug("user {}[{}] isn't updateable", name, id);
			}
		}
		
		this.logManageUser(name, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isUserLocked(User oper, User user) {
		boolean e = userDAO.isUserLocked(user);

		String name = user.getName();
		Long id = user.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is locked", name, id);
			} else {
				logger.debug("user {}[{}] isn't locked", name, id);
			}
		}

		this.logManageUser(name, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		return e;
	}

	@Override
	public boolean isUserRemoveable(User oper, User user) {
		boolean e = userDAO.isUserRemoveable(user);

		String name = user.getName();
		Long id = user.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is removeable", name, id);
			} else {
				logger.debug("user {}[{}] isn't removeable", name, id);
			}
		}

		this.logManageUser(name, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isUserDeleteable(User oper, User user) {
		boolean e = userDAO.isUserDeleteable(user);

		String name = user.getName();
		Long id = user.getId();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is deleteable", name, id);
			} else {
				logger.debug("user {}[{}] isn't deleteable", name, id);
			}
		}

		this.logManageUser(name, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public User getUser(User oper, long id) {
		User user = userDAO.queryUserById(id);
		
		String name = null;
		if (logger.isDebugEnabled()) {
			if (user != null) {
				name = user.getName();
				logger.debug("get user {}[{}]", name, id);
			} else {
				logger.debug("user of id {} is not exists", id);
			}
		}

		this.logManageUser(name, Action.GET, id, Status.SUCCESS, oper);
		return user;
	}
	
	@Override
	public User getUser(User oper, String name) {
		User user = userDAO.queryUserByName(name);
		
		Long id = null;
		if (logger.isDebugEnabled()) {
			if (user != null) {
				id = user.getId();
				logger.debug("get user {}[{}]", name, id);
			} else {
				logger.debug("user {} is not exists", name);
			}
		}

		this.logManageUser(name, Action.GET, id, Status.SUCCESS, oper);
		return user;
	}
	
	@Override
	public List<User> getUsers(User oper) {
		return this.getUsers(oper, null);
	}
	
	@Override
	public List<User> getUsers(User oper, QueryBase query) {
		List<User> users = userDAO.queryUsers(query);
		if (query != null) {
			query.setResults(users);
			long total = userDAO.countUsers(query);
			query.setTotalRow(total);
		}
		
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query users with parameters {}", query.getParameters());
			} else {
				logger.debug("query users");
			}
		}

		this.logManageUser(null, Action.QUERY, null, Status.SUCCESS, oper);
		return users;
	}
	
	@Override
	public long countUsers(User oper) {
		return this.countUsers(oper, null);
	}
	
	@Override
	public long countUsers(User oper, QueryBase query) {
		long c = userDAO.countUsers(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count users with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count users of {}", c);
			}
		}
		
		this.logManageUser(null, Action.COUNT, null, Status.SUCCESS, oper);
		return c;
	}
	
	private int doAddRole(User oper, User user, Role role)  throws AuthException {
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		if (!roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try add role {}[{}] for user {}[{}], but the role is not exists", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.ADD, null, Status.NOT_EXISTS, oper);
			throw new AuthException(Status.NOT_EXISTS, "角色[" + roleId + "]" + roleName + "不存在");
		}
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		userRole.put("owner", user.getOwner());
		userRole.put("modifier", user.getModifier());
		userRole.put("status", Status.OK);
		userRole.put("version", 0L);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		userRole.put("created", t);
		userRole.put("modified", t);
		
		if (userDAO.hasUserRole(userRole)) {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}] already has role {}[{}]", userName, userId, roleName, roleId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.ADD, null, Status.EXISTS, oper);
			return Status.SUCCESS;
		}
		
		short r = userDAO.addUserRole(userRole) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add role {}[{}] for user {}[{}]", roleName, roleId, userName, userId);
			} else {
				logger.debug("fail to add role {}[{}] for user {}[{}]", roleName, roleId, userName, userId);
			}
		}
		

		this.logManageUserRole(userName + "#" + roleName, Action.ADD, null, r, oper);
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addUserRole(User oper, User user, Role role)  throws AuthException {
		if (!userDAO.isUserExists(user)) {
			String userName = user.getName(), roleName = role.getName();
			if (logger.isDebugEnabled()) {
				logger.debug("try add role {}[{}] for user {}[{}], but the user is not exists", roleName, role.getId(), userName, user.getId());
			}
			
			this.logManageUserRole(userName + "#", Action.ADD, null, Status.NOT_EXISTS, oper);
			return Status.NOT_EXISTS;
		}
		return this.doAddRole(oper, user, role);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addUserRole(User oper, User user, List<Role> roles)  throws AuthException {
		Long userId = user.getId();
		String userName = user.getName();
		if (!userDAO.isUserExists(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try add roles for user {}[{}], but the user is not exists", userName, userId);
			}
			
			this.logManageUserRole(userName + "#", Action.ADD, null, Status.NOT_EXISTS, oper);
			throw new AuthException(Status.NOT_EXISTS, "用户[" + userId + "]" + userName + "不存在");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("add roles for user {}[{}]", userName, userId);
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.doAddRole(oper, user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockUserRole(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		userRole.put("modifier", oper != null ? oper.getId() : user.getModifier());
		
		short r = userDAO.lockUserRole(userRole) == 1 ? Status.SUCCESS : Status.ERROR;
		
		String userName = user.getName(), roleName = role.getName();
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock role {}[{}] for user {}[{}]", roleName, role.getId(), userName, user.getId());
			} else {
				logger.debug("fail to lock role {}[{}] for user {}[{}]", roleName, role.getId(), userName, user.getId());
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.LOCK, null, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockUserRole(User oper, User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("lock roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.lockUserRole(oper, user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockUserRole(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		userRole.put("modifier", oper != null ? oper.getId() : user.getModifier());
		
		short r = userDAO.unlockUserRole(userRole) == 1 ? Status.SUCCESS : Status.ERROR;
		
		String userName = user.getName(), roleName = role.getName();
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock role {}[{}] for user {}[{}]", roleName, role.getId(), userName, user.getId());
			} else {
				logger.debug("fail to unlock role {}[{}] for user {}[{}]", roleName, role.getId(), userName, user.getId());
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.UNLOCK, null, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockUserRole(User oper, User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("unlock roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.unlockUserRole(oper, user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeUserRole(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		short r = Status.SUCCESS;
		String userName = user.getName(), roleName = role.getName();
		if (userDAO.isUserRoleDeleteable(userRole)) {
			userRole.put("modifier", oper != null ? oper.getId() : user.getModifier());
			userDAO.removeUserRole(userRole);
			if (logger.isDebugEnabled()) {
				logger.debug("remove role {}[{}] from user {}[{}]", roleName, role.getId(), userName, user.getId());
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}]'s role {}[{}] isn't removeable", userName, user.getId(), roleName, role.getId());
			}
			r = Status.NO_REMOVE;
		}

		this.logManageUserRole(userName + "#" + roleName, Action.REMOVE, null, r, oper);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeUserRole(User oper, User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.removeUserRole(oper, user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteUserRole(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		short r = Status.SUCCESS;
		String userName = user.getName(), roleName = role.getName();
		if (userDAO.isUserRoleDeleteable(userRole)) {
			userDAO.deleteUserRole(userRole);
			if (logger.isDebugEnabled()) {
				logger.debug("delete role {}[{}] from user {}[{}]", roleName, role.getId(), userName, user.getId());
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}]'s role {}[{}] isn't deleteable", userName, user.getId(), roleName, role.getId());
			}
			r = Status.NO_DELETE;
		}

		this.logManageUserRole(userName + "#" + roleName, Action.REMOVE, null, r, oper);
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteUserRole(User oper, User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.deleteUserRole(oper, user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Override
	public boolean hasUserRole(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean h = userDAO.hasUserRole(userRole);
		
		String userName = user.getName(), roleName = role.getName();
		if (logger.isDebugEnabled()) {
			if (h) {
				logger.debug("user {}[{}] has role {}[{}]", userName, user.getId(), roleName, role.getId());
			} else {
				logger.debug("user {}[{}] hasn't role {}[{}]", userName, user.getId(), roleName, role.getId());
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.IS_EXISTS, null, Status.SUCCESS, oper);
		return h;
	}
	
	@Override
	public boolean isUserRoleUpdateable(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleUpdateable(userRole);
		
		String userName = user.getName(), roleName = role.getName();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is updateable", userName, user.getId(), roleName, role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't updateable", userName, user.getId(), roleName, role.getId());
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.IS_UPDATEABLE, null, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isUserRoleLocked(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleLocked(userRole);
		
		String userName = user.getName(), roleName = role.getName();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is locked", userName, user.getId(), roleName, role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't locked", userName, user.getId(), roleName, role.getId());
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.IS_LOCKED, null, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isUserRoleRemoveable(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleRemoveable(userRole);
		
		String userName = user.getName(), roleName = role.getName();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is removeable", userName, user.getId(), roleName, role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't removeable", userName, user.getId(), roleName, role.getId());
			}
		}
		
		this.logManageUserRole(userName + "#" + roleName, Action.IS_REMOVEABLE, null, Status.SUCCESS, oper);
		return e;
	}
	
	@Override
	public boolean isUserRoleDeleteable(User oper, User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleDeleteable(userRole);
		
		String userName = user.getName(), roleName = role.getName();
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is deleteable", userName, user.getId(), roleName, role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't deleteable", userName, user.getId(), roleName, role.getId());
			}
		}
		
		this.logManageUserRole(userName + "#" + roleName, Action.IS_DELETEABLE, null, Status.SUCCESS, oper);
		return e;
	}
	
	private String encrypt(String data) {
//		return new MD5().encrypt(new SHA1().encrypt(data));
		return data;
	}
	
	private void logManageUser(String content, Short action, Long obj, Short status, User oper) {
		this.logManage(content, action, ManageLog.TYPE_USER, obj, status, oper);
	}
	
	private void logManageUserRole(String content, Short action, Long obj, Short status, User oper) {
		this.logManage(content, action, ManageLog.TYPE_USER_ROLE, obj, status, oper);
	}
	
	private void logManage(String content, Short action, Short type, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, type, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

}
