package org.xhome.xauth.core.service;

import java.util.List;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.AuthException;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jun 14, 2013 10:13:12 PM
 */
public interface UserService {

	public User auth(User user, String address, short agent, String number) throws AuthException;
	
	public int addUser(User oper, User user) throws AuthException;
	
	public int updateUser(User oper, User user);
	
	public int changePassword(User oper, User user, String newPassword);
	
	public int lockUser(User oper, User user);
	
	public int unlockUser(User oper, User user);
	
	public int removeUser(User oper, User user);
	
	public int deleteUser(User oper, User user);
	
	public boolean isUserExists(User oper, User user);
	
	public boolean isUserUpdateable(User oper, User user);
	
	public boolean isUserLocked(User oper, User user);

	public boolean isUserRemoveable(User oper, User user);
	
	public boolean isUserDeleteable(User oper, User user);
	
	public User getUser(User oper, long id);
	
	public User getUser(User oper, String name);
	
	public List<User> getUsers(User oper);
	
	public List<User> getUsers(User oper, QueryBase query);
	
	public long countUsers(User oper);
	
	public long countUsers(User oper, QueryBase query);
	
	public int addUserRole(User oper, User user, Role role) throws AuthException;
	
	public int addUserRole(User oper, User user, List<Role> roles) throws AuthException;
	
	public int lockUserRole(User oper, User user, Role role);
	
	public int lockUserRole(User oper, User user, List<Role> roles);
	
	public int unlockUserRole(User oper, User user, Role role);
	
	public int unlockUserRole(User oper, User user, List<Role> roles);
	
	public int removeUserRole(User oper, User user, Role role);
	
	public int removeUserRole(User oper, User user, List<Role> roles);
	
	public int deleteUserRole(User oper, User user, Role role);
	
	public int deleteUserRole(User oper, User user, List<Role> roles);
	
	public boolean hasUserRole(User oper, User user, Role role);
	
	public boolean isUserRoleUpdateable(User oper, User user, Role role);
	
	public boolean isUserRoleLocked(User oper, User user, Role role);

	public boolean isUserRoleRemoveable(User oper, User user, Role role);
	
	public boolean isUserRoleDeleteable(User oper, User user, Role role);
	
}
