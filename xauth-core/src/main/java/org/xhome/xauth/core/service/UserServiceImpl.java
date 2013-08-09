package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.Status;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthException;
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
	public int addUser(User user)  throws AuthException {
		String name = user.getName();
		if (userDAO.isUserExists(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add user {}, but it's already exists", name);
			}
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
			r = this.addUserRole(user, user.getRoles());
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
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateUser(User user) {
		Long id = user.getId();
		User old = userDAO.queryUserById(id);
		String name = user.getName();
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update user {}[{}], but it's not exists", name, id);
			}
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(user.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update user {}[{}], but version not match", oldName, id);
			}
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update user {}[{}]", oldName, id);
			}
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
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int changePassword(User user, String newPassword) {
		Long id = user.getId();
		User old = userDAO.queryUserById(id);
		String name = user.getName();
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but it's not exists", name, id);
			}
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(user.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but version not match", oldName, id);
			}
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update user {}[{}]", oldName, id);
			}
			return status;
		}
		
		String oldPassword = this.encrypt(user.getPassword());
		if (!oldPassword.equals(old.getPassword())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but old password not match", oldName, id);
			}
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
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockUser(User user) {
		int r = userDAO.lockUser(user) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock user {}[{}]", user.getName(), user.getId());
			} else {
				logger.debug("fail to lock user {}[{}]", user.getName(), user.getId());
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockUser(User user) {
		int r = userDAO.unlockUser(user) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock user {}[{}]", user.getName(), user.getId());
			} else {
				logger.debug("fail to unlock user {}[{}]", user.getName(), user.getId());
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeUser(User user) {
		if (userDAO.isUserDeleteable(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove user {}[{}]", user.getName(), user.getId());
			}
			userDAO.removeUser(user);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}] isn't removeable", user.getName(), user.getId());
			}
		}
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteUser(User user) {
		if (userDAO.isUserDeleteable(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete user {}[{}]", user.getName(), user.getId());
			}
			userDAO.deleteUser(user);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}] isn't deleteable", user.getName(), user.getId());
			}
		}
		return Status.SUCCESS;
	}
	
	@Override
	public boolean isUserExists(User user) {
		boolean e = userDAO.isUserExists(user);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of user {}", user.getName());
			} else {
				logger.debug("not exists of user {}", user.getName());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isUserUpdateable(User user) {
		boolean e = userDAO.isUserUpdateable(user);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is updateable", user.getName(), user.getId());
			} else {
				logger.debug("user {}[{}] isn't updateable", user.getName(), user.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isUserLocked(User user) {
		boolean e = userDAO.isUserLocked(user);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is locked", user.getName(), user.getId());
			} else {
				logger.debug("user {}[{}] isn't locked", user.getName(), user.getId());
			}
		}
		
		return e;
	}

	@Override
	public boolean isUserRemoveable(User user) {
		boolean e = userDAO.isUserRemoveable(user);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is removeable", user.getName(), user.getId());
			} else {
				logger.debug("user {}[{}] isn't removeable", user.getName(), user.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isUserDeleteable(User user) {
		boolean e = userDAO.isUserDeleteable(user);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is deleteable", user.getName(), user.getId());
			} else {
				logger.debug("user {}[{}] isn't deleteable", user.getName(), user.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public User getUser(long id) {
		User user = userDAO.queryUserById(id);
		
		if (logger.isDebugEnabled()) {
			if (user != null) {
				logger.debug("get user {}[{}]", user.getName(), user.getId());
			} else {
				logger.debug("user of id {} is not exists", id);
			}
		}
		
		return user;
	}
	
	@Override
	public User getUser(String name) {
		User user = userDAO.queryUserByName(name);
		
		if (logger.isDebugEnabled()) {
			if (user != null) {
				logger.debug("get user {}[{}]", name, user.getId());
			} else {
				logger.debug("user {} is not exists", name);
			}
		}
		
		return user;
	}
	
	@Override
	public List<User> getUsers() {
		return this.getUsers(null);
	}
	
	@Override
	public List<User> getUsers(QueryBase query) {
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
		
		return users;
	}
	
	@Override
	public long countUsers() {
		return this.countUsers(null);
	}
	
	@Override
	public long countUsers(QueryBase query) {
		long c = userDAO.countUsers(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count users with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count users of {}", c);
			}
		}
		return c;
	}
	
	private int doAddRole(User user, Role role)  throws AuthException {
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		if (!roleDAO.isRoleExists(role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try add role {}[{}] for user {}[{}], but the role is not exists", roleName, roleId, userName, userId);
			}
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
			return Status.SUCCESS;
		}
		
		int r = userDAO.addUserRole(userRole) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add role {}[{}] for user {}[{}]", roleName, roleId, userName, userId);
			} else {
				logger.debug("fail to add role {}[{}] for user {}[{}]", roleName, roleId, userName, userId);
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addUserRole(User user, Role role)  throws AuthException {
		if (!userDAO.isUserExists(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try add role {}[{}] for user {}[{}], but the user is not exists", role.getName(), role.getId(), user.getName(), user.getId());
			}
			return Status.NOT_EXISTS;
		}
		return this.doAddRole(user, role);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addUserRole(User user, List<Role> roles)  throws AuthException {
		Long userId = user.getId();
		String userName = user.getName();
		if (!userDAO.isUserExists(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try add roles for user {}[{}], but the user is not exists", userName, userId);
			}
			throw new AuthException(Status.NOT_EXISTS, "用户[" + userId + "]" + userName + "不存在");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("add roles for user {}[{}]", userName, userId);
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.doAddRole(user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockUserRole(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		userRole.put("modifier", user.getModifier());
		
		int r = userDAO.lockUserRole(userRole);
		r = r == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock role {}[{}] for user {}[{}]", role.getName(), role.getId(), user.getName(), user.getId());
			} else {
				logger.debug("fail to lock role {}[{}] for user {}[{}]", role.getName(), role.getId(), user.getName(), user.getId());
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockUserRole(User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("lock roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.lockUserRole(user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockUserRole(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		userRole.put("modifier", user.getModifier());
		
		int r = userDAO.unlockUserRole(userRole) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock role {}[{}] for user {}[{}]", role.getName(), role.getId(), user.getName(), user.getId());
			} else {
				logger.debug("fail to unlock role {}[{}] for user {}[{}]", role.getName(), role.getId(), user.getName(), user.getId());
			}
		}
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockUserRole(User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("unlock roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.unlockUserRole(user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeUserRole(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		if (userDAO.isUserRoleDeleteable(userRole)) {
			userRole.put("modifier", user.getModifier());
			userDAO.removeUserRole(userRole);
			if (logger.isDebugEnabled()) {
				logger.debug("remove role {}[{}] from user {}[{}]", role.getName(), role.getId(), user.getName(), user.getId());
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}]'s role {}[{}] isn't removeable", user.getName(), user.getId(), role.getName(), role.getId());
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeUserRole(User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.removeUserRole(user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteUserRole(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		if (userDAO.isUserRoleDeleteable(userRole)) {
			userDAO.deleteUserRole(userRole);
			if (logger.isDebugEnabled()) {
				logger.debug("delete role {}[{}] from user {}[{}]", role.getName(), role.getId(), user.getName(), user.getId());
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}]'s role {}[{}] isn't deleteable", user.getName(), user.getId(), role.getName(), role.getId());
			}
		}
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteUserRole(User user, List<Role> roles) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete roles for user {}[{}]", user.getName(), user.getId());
		}
		
		if (roles != null) {
			int r = Status.ERROR;
			for (Role role : roles) {
				r = this.deleteUserRole(user, role);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Override
	public boolean hasUserRole(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean h = userDAO.hasUserRole(userRole);
		
		if (logger.isDebugEnabled()) {
			if (h) {
				logger.debug("user {}[{}] has role {}[{}]", user.getName(), user.getId(), role.getName(), role.getId());
			} else {
				logger.debug("user {}[{}] hasn't role {}[{}]", user.getName(), user.getId(), role.getName(), role.getId());
			}
		}
		
		return h;
	}
	
	@Override
	public boolean isUserRoleUpdateable(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleUpdateable(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is updateable", user.getName(), user.getId(), role.getName(), role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't updateable", user.getName(), user.getId(), role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isUserRoleLocked(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleLocked(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is locked", user.getName(), user.getId(), role.getName(), role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't locked", user.getName(), user.getId(), role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isUserRoleRemoveable(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleRemoveable(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is removeable", user.getName(), user.getId(), role.getName(), role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't removeable", user.getName(), user.getId(), role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	@Override
	public boolean isUserRoleDeleteable(User user, Role role) {
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleDeleteable(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is deleteable", user.getName(), user.getId(), role.getName(), role.getId());
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't deleteable", user.getName(), user.getId(), role.getName(), role.getId());
			}
		}
		
		return e;
	}
	
	private String encrypt(String data) {
//		return new MD5().encrypt(new SHA1().encrypt(data));
		return data;
	}
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

}
