package org.xhome.xauth.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthException;
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.auth.UserAuth;
import org.xhome.xauth.core.dao.RoleDAO;
import org.xhome.xauth.core.dao.UserDAO;
import org.xhome.xauth.core.listener.UserAuthListener;
import org.xhome.xauth.core.listener.UserManageListener;
import org.xhome.xauth.core.listener.UserRoleManageListener;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 201312:54:23 PM
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired(required = false)
	private UserDAO	userDAO;
	@Autowired(required = false)
	private RoleDAO	roleDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private AuthLogService authLogService;
	@Autowired(required = false)
	private UserCryptoService userCryptoService;
	@Autowired(required = false)
	private Map<String, UserAuth> userAuthMaps;
	
	@Autowired(required = false)
	private List<UserManageListener> userManageListeners;
	@Autowired(required = false)
	private List<UserRoleManageListener> userRoleManageListeners;
	@Autowired(required = false)
	private List<UserAuthListener> userAuthListeners;
	
	private Logger  logger;
	
	public UserServiceImpl() {
		logger = LoggerFactory.getLogger(UserService.class);
	}
	
	@Override
	public User auth(User user, String address, short agent, String number) throws AuthException {
		String name = user.getName();
		
		if (!this.beforeUserAuth(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to auth user {}, but it's blocked", name);
			}
			
			this.logAuth(user, address, agent, number, Status.BLOCKED);
			this.afterUserAuth(user, user, Status.BLOCKED);
			throw new AuthException(Status.BLOCKED, "禁止认证");
		}
		
		UserAuth userAuth = null;
		String method = user.getMethod();
		if (userAuthMaps != null) {
			userAuth = userAuthMaps.get(method);
		}
		if (userAuth != null) {
			try {
				User u = userAuth.auth(user);
				
				if (logger.isDebugEnabled()) {
					logger.debug("success auth user {} with {} method", name, method);
				}
				this.logAuth(user, address, agent, number, Status.SUCCESS);
				this.afterUserAuth(user, u, Status.SUCCESS);
				return u;
			} catch (AuthException ae) {
				if (logger.isDebugEnabled()) {
					logger.debug("failed auth user {} with {} method", name, method);
				}
				
				this.logAuth(user, address, agent, number, Status.ERROR);
				this.afterUserAuth(user, user, Status.ERROR);
				throw ae;
			}
		}
		
		// 采用默认方法进行认证
		user.setMethod("DEFAULT");
		User u = userDAO.queryUserByName(name);
		if (u == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to auth user {}, but it's not exists", name);
			}
			this.logAuth(user, address, agent, number, Status.NOT_EXISTS);
			this.afterUserAuth(user, user, Status.NOT_EXISTS);
			throw new AuthException(Status.NOT_EXISTS, "用户不存在");
		}
		
		short status = u.getStatus();
		if (status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to auth user {}, but it's locked", name);
			}
			this.logAuth(user, address, agent, number, Status.LOCK);
			this.afterUserAuth(user, u, Status.LOCK);
			throw new AuthException(status, "用户已锁定");
		}
		
		userCryptoService.crypto(user);
		if (u.getPassword().equals(user.getPassword())) {
			if (logger.isDebugEnabled()) {
				logger.debug("success auth user {}", name);
			}
			
			this.logAuth(user, address, agent, number, Status.SUCCESS);
			this.afterUserAuth(user, u, Status.SUCCESS);
			return u;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("try to auth user {}, but password error", name);
			}
			this.logAuth(user, address, agent, number, Status.PASSWD_NOT_MATCH);
			this.afterUserAuth(user, u, Status.PASSWD_NOT_MATCH);
			throw new AuthException(Status.PASSWD_NOT_MATCH, "密码错误");
		}
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addUser(User oper, User user)  throws AuthException {
		String name = user.getName();
		
		if (!this.beforeUserManage(oper, Action.ADD, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add user {}, but it's blocked", name);
			}
			
			this.logManageUser(name, Action.ADD, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.ADD, Status.BLOCKED, user);
			return Status.BLOCKED;
		}
		
		if (userDAO.isUserExists(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add user {}, but it's already exists", name);
			}
			
			this.logManageUser(name, Action.ADD, null, Status.EXISTS, oper);
			this.afterUserManage(oper, Action.ADD, Status.EXISTS, user);
			return Status.EXISTS;
		}
		
		userCryptoService.crypto(user);
		user.setStatus(Status.OK);
		user.setVersion((short)0);
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
		this.afterUserManage(oper, Action.ADD, (short) r, user);
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateUser(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.UPDATE, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.UPDATE, Status.BLOCKED, user);
			return Status.BLOCKED;
		}
		
		User old = userDAO.queryUserById(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update user {}[{}], but it's not exists", name, id);
			}
			
			this.logManageUser(name, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterUserManage(oper, Action.UPDATE, Status.NOT_EXISTS, user);
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(user.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update user {}[{}], but version not match", oldName, id);
			}
			
			this.logManageUser(oldName, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterUserManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, user);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update user {}[{}]", oldName, id);
			}
			
			this.logManageUser(oldName, Action.UPDATE, id, status, oper);
			this.afterUserManage(oper, Action.UPDATE, status, user);
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
		this.afterUserManage(oper, Action.UPDATE, (short) r, user);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int changePassword(User oper, User user, String newPassword) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.UPDATE, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password of user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.UPDATE, Status.BLOCKED, user);
			return Status.BLOCKED;
		}
		
		User old = userDAO.queryUserById(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but it's not exists", name, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterUserManage(oper, Action.UPDATE, Status.NOT_EXISTS, user);
			return Status.NOT_EXISTS;
		}
		
		String oldName = old.getName();
		
		if (!old.getVersion().equals(user.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but version not match", oldName, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterUserManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, user);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update user {}[{}]", oldName, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, status, oper);
			this.afterUserManage(oper, Action.UPDATE, status, user);
			return status;
		}
		
		userCryptoService.crypto(user);
		if (!old.getPassword().equals(user.getPassword())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to change password for user {}[{}], but old password not match", oldName, id);
			}
			
			this.logManageUser(name + "#PASSWORD", Action.UPDATE, id, Status.PASSWD_NOT_MATCH, oper);
			this.afterUserManage(oper, Action.UPDATE, Status.PASSWD_NOT_MATCH, user);
			return Status.PASSWD_NOT_MATCH;
		}
		
		user.setPassword(newPassword);
		userCryptoService.crypto(user);
		old.setPassword(user.getPassword());
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
		this.afterUserManage(oper, Action.UPDATE, (short) r, user);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockUser(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.LOCK, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.LOCK, Status.BLOCKED, user);
			return Status.BLOCKED;
		}
		
		short r = userDAO.lockUser(user) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock user {}[{}]", name, id);
			} else {
				logger.debug("fail to lock user {}[{}]", name, id);
			}
		}
		
		this.logManageUser(name, Action.LOCK, id, r, oper);
		this.afterUserManage(oper, Action.LOCK, r, user);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockUser(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.UNLOCK, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.UNLOCK, Status.BLOCKED, user);
			return Status.BLOCKED;
		}
		
		short r = userDAO.unlockUser(user) == 1 ? Status.SUCCESS : Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock user {}[{}]", name, id);
			} else {
				logger.debug("fail to unlock user {}[{}]", name, id);
			}
		}

		this.logManageUser(name, Action.UNLOCK, id, r, oper);
		this.afterUserManage(oper, Action.UNLOCK, r, user);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeUser(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.REMOVE, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.REMOVE, Status.BLOCKED, user);
			return Status.BLOCKED;
		}
		
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
		this.afterUserManage(oper, Action.REMOVE, r, user);
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteUser(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.DELETE, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.DELETE, Status.BLOCKED, user);
			return Status.BLOCKED;
		}
		
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
		this.afterUserManage(oper, Action.DELETE, r, user);
		return Status.SUCCESS;
	}
	
	@Override
	public boolean isUserExists(User oper, User user) {
		String name = user.getName();
		
		if (!this.beforeUserManage(oper, Action.IS_EXISTS, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge exists of user {}, but it's blocked", name);
			}
			
			this.logManageUser(name, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.IS_EXISTS, Status.BLOCKED, user);
			return false;
		}
		
		boolean e = userDAO.isUserExists(user);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of user {}", name);
			} else {
				logger.debug("not exists of user {}", name);
			}
		}

		this.logManageUser(name, Action.IS_EXISTS, user.getId(), Status.SUCCESS, oper);
		this.afterUserManage(oper, Action.IS_EXISTS, Status.SUCCESS, user);
		return e;
	}
	
	@Override
	public boolean isUserUpdateable(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.IS_UPDATEABLE, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge updateable of user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, user);
			return false;
		}
		
		boolean e = userDAO.isUserUpdateable(user);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is updateable", name, id);
			} else {
				logger.debug("user {}[{}] isn't updateable", name, id);
			}
		}
		
		this.logManageUser(name, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterUserManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, user);
		return e;
	}
	
	@Override
	public boolean isUserLocked(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.IS_LOCKED, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge locked of user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.IS_LOCKED, Status.BLOCKED, user);
			return false;
		}
		
		boolean e = userDAO.isUserLocked(user);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is locked", name, id);
			} else {
				logger.debug("user {}[{}] isn't locked", name, id);
			}
		}

		this.logManageUser(name, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterUserManage(oper, Action.IS_LOCKED, Status.SUCCESS, user);
		return e;
	}

	@Override
	public boolean isUserRemoveable(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.IS_REMOVEABLE, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge removeable of user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, user);
			return false;
		}
		
		boolean e = userDAO.isUserRemoveable(user);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is removeable", name, id);
			} else {
				logger.debug("user {}[{}] isn't removeable", name, id);
			}
		}

		this.logManageUser(name, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterUserManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, user);
		return e;
	}
	
	@Override
	public boolean isUserDeleteable(User oper, User user) {
		String name = user.getName();
		Long id = user.getId();
		
		if (!this.beforeUserManage(oper, Action.IS_DELETEABLE, user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge deleteable of user {}[{}], but it's blocked", name, id);
			}
			
			this.logManageUser(name, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, user);
			return false;
		}
		
		boolean e = userDAO.isUserDeleteable(user);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}] is deleteable", name, id);
			} else {
				logger.debug("user {}[{}] isn't deleteable", name, id);
			}
		}

		this.logManageUser(name, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterUserManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, user);
		return e;
	}
	
	@Override
	public User getUser(User oper, long id) {
		if (!this.beforeUserManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get user of id {}, but it's blocked", id);
			}
			
			this.logManageUser("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}
		
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
		this.afterUserManage(oper, Action.GET, Status.SUCCESS, user, id);
		return user;
	}
	
	@Override
	public User getUser(User oper, String name) {
		if (!this.beforeUserManage(oper, Action.GET, null, name)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get user {}, but it's blocked", name);
			}
			
			this.logManageUser(name, Action.GET, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.GET, Status.BLOCKED, null, name);
			return null;
		}
		
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
		this.afterUserManage(oper, Action.GET, Status.SUCCESS, user, name);
		return user;
	}
	
	@Override
	public List<User> getUsers(User oper) {
		return this.getUsers(oper, null);
	}
	
	@Override
	public List<User> getUsers(User oper, QueryBase query) {
		if (!this.beforeUserManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query users, but it's blocked");
			}
			
			this.logManageUser(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
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
		this.afterUserManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return users;
	}
	
	@Override
	public long countUsers(User oper) {
		return this.countUsers(oper, null);
	}
	
	@Override
	public long countUsers(User oper, QueryBase query) {
		if (!this.beforeUserManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count users, but it's blocked");
			}
			
			this.logManageUser(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterUserManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = userDAO.countUsers(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count users with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count users of {}", c);
			}
		}
		
		this.logManageUser(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterUserManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private int doAddRole(User oper, User user, Role role)  throws AuthException {
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.ADD, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.ADD, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.ADD, Status.BLOCKED, user, role);
			return Status.BLOCKED;
		}
		
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
		this.afterUserRoleManage(oper, Action.ADD, r, user, role);
		
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
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.LOCK, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.LOCK, Status.BLOCKED, user, role);
			return Status.BLOCKED;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		userRole.put("modifier", oper != null ? oper.getId() : user.getModifier());
		
		short r = userDAO.lockUserRole(userRole) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock role {}[{}] for user {}[{}]", roleName, roleId, userName, userName);
			} else {
				logger.debug("fail to lock role {}[{}] for user {}[{}]", roleName, roleId, userName, userName);
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.LOCK, null, r, oper);
		this.afterUserRoleManage(oper, Action.LOCK, r, user, role);
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
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.UNLOCK, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.UNLOCK, Status.BLOCKED, user, role);
			return Status.BLOCKED;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		userRole.put("modifier", oper != null ? oper.getId() : user.getModifier());
		
		short r = userDAO.unlockUserRole(userRole) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock role {}[{}] for user {}[{}]", roleName, roleId, userName, userId);
			} else {
				logger.debug("fail to unlock role {}[{}] for user {}[{}]", roleName, roleId, userName, userId);
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.UNLOCK, null, r, oper);
		this.afterUserRoleManage(oper, Action.UNLOCK, r, user, role);
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
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.REMOVE, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.REMOVE, Status.BLOCKED, user, role);
			return Status.BLOCKED;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		short r = Status.SUCCESS;
		if (userDAO.isUserRoleDeleteable(userRole)) {
			userRole.put("modifier", oper != null ? oper.getId() : user.getModifier());
			userDAO.removeUserRole(userRole);
			if (logger.isDebugEnabled()) {
				logger.debug("remove role {}[{}] from user {}[{}]", roleName, roleId, userName, userId);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}]'s role {}[{}] isn't removeable", userName, userId, roleName, roleId);
			}
			r = Status.NO_REMOVE;
		}

		this.logManageUserRole(userName + "#" + roleName, Action.REMOVE, null, r, oper);
		this.afterUserRoleManage(oper, Action.REMOVE, r, user, role);
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
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.DELETE, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.DELETE, Status.BLOCKED, user, role);
			return Status.BLOCKED;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		short r = Status.SUCCESS;
		if (userDAO.isUserRoleDeleteable(userRole)) {
			userDAO.deleteUserRole(userRole);
			if (logger.isDebugEnabled()) {
				logger.debug("delete role {}[{}] from user {}[{}]", roleName, roleId, userName, userId);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("user {}[{}]'s role {}[{}] isn't deleteable", userName, userId, roleName, roleId);
			}
			r = Status.NO_DELETE;
		}

		this.logManageUserRole(userName + "#" + roleName, Action.DELETE, null, r, oper);
		this.afterUserRoleManage(oper, Action.DELETE, r, user, role);
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
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.IS_EXISTS, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge exists of role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.IS_EXISTS, Status.BLOCKED, user, role);
			return false;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean h = userDAO.hasUserRole(userRole);
		
		if (logger.isDebugEnabled()) {
			if (h) {
				logger.debug("user {}[{}] has role {}[{}]", userName, userId, roleName, roleId);
			} else {
				logger.debug("user {}[{}] hasn't role {}[{}]", userName, userId, roleName, roleId);
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.IS_EXISTS, null, Status.SUCCESS, oper);
		this.afterUserRoleManage(oper, Action.IS_EXISTS, Status.SUCCESS, user, role);
		return h;
	}
	
	@Override
	public boolean isUserRoleUpdateable(User oper, User user, Role role) {
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.IS_UPDATEABLE, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge updateable of role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, user, role);
			return false;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleUpdateable(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is updateable", userName, userId, roleName, roleId);
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't updateable", userName, userId, roleName, roleId);
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.IS_UPDATEABLE, null, Status.SUCCESS, oper);
		this.afterUserRoleManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, user, role);
		return e;
	}
	
	@Override
	public boolean isUserRoleLocked(User oper, User user, Role role) {
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.IS_LOCKED, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge locked of role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.IS_LOCKED, Status.BLOCKED, user, role);
			return false;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleLocked(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is locked", userName, userId, roleName, roleId);
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't locked", userName, userId, roleName, roleId);
			}
		}

		this.logManageUserRole(userName + "#" + roleName, Action.IS_LOCKED, null, Status.SUCCESS, oper);
		this.afterUserRoleManage(oper, Action.IS_LOCKED, Status.SUCCESS, user, role);
		return e;
	}
	
	@Override
	public boolean isUserRoleRemoveable(User oper, User user, Role role) {
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.IS_REMOVEABLE, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge removeable of role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, user, role);
			return false;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleRemoveable(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is removeable", userName, userId, roleName, roleId);
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't removeable", userName, userId, roleName, roleId);
			}
		}
		
		this.logManageUserRole(userName + "#" + roleName, Action.IS_REMOVEABLE, null, Status.SUCCESS, oper);
		this.afterUserRoleManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, user, role);
		return e;
	}
	
	@Override
	public boolean isUserRoleDeleteable(User oper, User user, Role role) {
		Long roleId = role.getId(), userId = user.getId();
		String roleName = role.getName(), userName = user.getName();
		
		if (!this.beforeUserRoleManage(oper, Action.IS_DELETEABLE, user, role)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge deleteable of role {}[{}] for user {}[{}], but it's blocked", roleName, roleId, userName, userId);
			}
			
			this.logManageUserRole(userName + "#" + roleName, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterUserRoleManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, user, role);
			return false;
		}
		
		Map<String, Object> userRole = new HashMap<String, Object>();
		userRole.put("user", user);
		userRole.put("role", role);
		
		boolean e = userDAO.isUserRoleDeleteable(userRole);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("user {}[{}]'s role {}[{}] is deleteable", userName, userId, roleName, roleId);
			} else {
				logger.debug("user {}[{}]'s role {}[{}] isn't deleteable", userName, userId, roleName, roleId);
			}
		}
		
		this.logManageUserRole(userName + "#" + roleName, Action.IS_DELETEABLE, null, Status.SUCCESS, oper);
		this.afterUserRoleManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, user, role);
		return e;
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
	
	private void logAuth(User user, String address, short agent, String number, Short status) {
		AuthLog authLog = new AuthLog(user, user.getMethod(), address, agent, number);
		authLog.setStatus(status);
		authLogService.logAuth(authLog);
	}
	
	private boolean beforeUserManage(User oper, short action, User user, Object ...args) {
		if (userManageListeners != null) {
			for (UserManageListener listener : userManageListeners) {
				if (!listener.beforeUserManage(oper, action, user, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterUserManage(User oper, short action, short result, User user, Object ...args) {
		if (userManageListeners != null) {
			for (UserManageListener listener : userManageListeners) {
				listener.afterUserManage(oper, action, result, user, args);
			}
		}
	}
	
	private boolean beforeUserRoleManage(User oper, short action, User user, Role role, Object ...args) {
		if (userRoleManageListeners != null) {
			for (UserRoleManageListener listener : userRoleManageListeners) {
				if (!listener.beforeUserRoleManage(oper, action, user, role, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterUserRoleManage(User oper, short action, short result, User user, Role role, Object ...args) {
		if (userRoleManageListeners != null) {
			for (UserRoleManageListener listener : userRoleManageListeners) {
				listener.afterUserRoleManage(oper, action, result, user, role, args);
			}
		}
	}
	
	private boolean beforeUserAuth(User user) {
		if (userAuthListeners != null) {
			for (UserAuthListener listener : userAuthListeners) {
				if (!listener.beforeUserAuth(user)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterUserAuth(User before, User after, short result) {
		if (userAuthListeners != null) {
			for (UserAuthListener listener : userAuthListeners) {
				listener.afterUserAuth(before, after, result);
			}
		}
	}
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public UserDAO getUserDAO() {
		return this.userDAO;
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
	
	public void setAuthLogService(AuthLogService authLogService) {
		this.authLogService = authLogService;
	}

	public AuthLogService getAuthLogService() {
		return authLogService;
	}
	
	public void setUserCryptoService(UserCryptoService userCryptoService) {
		this.userCryptoService = userCryptoService;
	}

	public UserCryptoService getUserCryptoService() {
		return userCryptoService;
	}
	
	public void setUserAuthMaps(Map<String, UserAuth> userAuthMaps) {
		this.userAuthMaps = userAuthMaps;
	}
	
	public Map<String, UserAuth> getUserAuthMaps() {
		return this.userAuthMaps;
	}
	
	public void registerUserAuth(String method, UserAuth userAuth) {
		if (userAuthMaps == null) {
			synchronized (this) {
				if (userAuthMaps == null) {
					userAuthMaps = new HashMap<String, UserAuth>();
				}
			}
		}
		userAuthMaps.put(method, userAuth);
	}
	
	public void setUserManageListeners(List<UserManageListener> userManageListeners) {
		this.userManageListeners = userManageListeners;
	}

	public List<UserManageListener> getUserManageListeners() {
		return userManageListeners;
	}
	
	public void registerUserManageListener(UserManageListener userManageListener) {
		if (userManageListeners == null) {
			synchronized (this) {
				if (userManageListeners == null) {
					userManageListeners = new ArrayList<UserManageListener>();
				}
			}
		}
		userManageListeners.add(userManageListener);
	}

	public void setUserRoleManageListeners(List<UserRoleManageListener> userRoleManageListeners) {
		this.userRoleManageListeners = userRoleManageListeners;
	}

	public List<UserRoleManageListener> getUserRoleManageListeners() {
		return userRoleManageListeners;
	}
	
	public void registerUserRoleManageListener(UserRoleManageListener userRoleManageListener) {
		if (userRoleManageListeners == null) {
			synchronized (this) {
				if (userRoleManageListeners == null) {
					userRoleManageListeners = new ArrayList<UserRoleManageListener>();
				}
			}
		}
		userRoleManageListeners.add(userRoleManageListener);
	}
	
	public void setUserAuthListeners(List<UserAuthListener> userAuthListeners) {
		this.userAuthListeners = userAuthListeners;
	}

	public List<UserAuthListener> getUserAuthListeners() {
		return userAuthListeners;
	}
	
	public void registerUserAuthListener(UserAuthListener userAuthListener) {
		if (userAuthListeners == null) {
			synchronized (this) {
				if (userAuthListeners == null) {
					userAuthListeners = new ArrayList<UserAuthListener>();
				}
			}
		}
		userAuthListeners.add(userAuthListener);
	}

}
