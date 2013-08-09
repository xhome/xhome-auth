package org.xhome.xauth.core.service;

import java.util.List;

import org.xhome.common.query.QueryBase;
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

	public User login(User user) throws AuthException;
	
	public int addUser(User user) throws AuthException;
	
	public int updateUser(User user);
	
	public int changePassword(User user, String newPassword);
	
	public int lockUser(User user);
	
	public int unlockUser(User user);
	
	public int removeUser(User user);
	
	public int deleteUser(User user);
	
	public boolean isUserExists(User user);
	
	public boolean isUserUpdateable(User user);
	
	public boolean isUserLocked(User user);

	public boolean isUserRemoveable(User user);
	
	public boolean isUserDeleteable(User user);
	
	public User getUser(long id);
	
	public User getUser(String name);
	
	public List<User> getUsers();
	
	public List<User> getUsers(QueryBase query);
	
	public long countUsers();
	
	public long countUsers(QueryBase query);
	
	public int addUserRole(User user, Role role) throws AuthException;
	
	public int addUserRole(User user, List<Role> roles) throws AuthException;
	
	public int lockUserRole(User user, Role role);
	
	public int lockUserRole(User user, List<Role> roles);
	
	public int unlockUserRole(User user, Role role);
	
	public int unlockUserRole(User user, List<Role> roles);
	
	public int removeUserRole(User user, Role role);
	
	public int removeUserRole(User user, List<Role> roles);
	
	public int deleteUserRole(User user, Role role);
	
	public int deleteUserRole(User user, List<Role> roles);
	
	public boolean hasUserRole(User user, Role role);
	
	public boolean isUserRoleUpdateable(User user, Role role);
	
	public boolean isUserRoleLocked(User user, Role role);

	public boolean isUserRoleRemoveable(User user, Role role);
	
	public boolean isUserRoleDeleteable(User user, Role role);
	
}
