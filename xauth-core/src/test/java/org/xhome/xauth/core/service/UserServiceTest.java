package org.xhome.xauth.core.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.xhome.common.constant.Agent;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthException;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.AbstractTest;
import org.xhome.xauth.core.listener.TestUserAuthListener;
import org.xhome.xauth.core.listener.TestUserManageListener;
import org.xhome.xauth.core.listener.TestUserRoleManageListener;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 20131:33:15 PM
 */
public class UserServiceTest extends AbstractTest {
	
	private UserService	userService;
	private RoleService	roleService;
	
	public UserServiceTest() {
		userService = context.getBean(UserServiceImpl.class);
		roleService = context.getBean(RoleServiceImpl.class);
		
		oper.setId(102L);
		
		((UserServiceImpl)userService).registerUserManageListener(new TestUserManageListener());
		((UserServiceImpl)userService).registerUserRoleManageListener(new TestUserRoleManageListener());
		((UserServiceImpl)userService).registerUserAuthListener(new TestUserAuthListener());
		((UserServiceImpl)userService).registerUserAuth("test", new TestUserAuth());
	}
	
	@Test
	public void testAddUser() {
		try {
			User user = new User("jhata", "abcdef");
			user.setNick("Jhat");
			user.setMethod("SHA");
			user.setEmail("cpf624@126.com");
			user.setOwner(1L);
			user.setModifier(1L);
			
			Role role = null;
			List<Role> roles = new ArrayList<Role>();
			
			role = new Role("ADMIN");
			role.setId(1L);
			roles.add(role);
			
			user.setRoles(roles);
			userService.addUser(oper, user);
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	@Test
	public void testAuth() {
		User user = new User("jhata", "abcdef");
		try {
//			user.setMethod("test");
			userService.auth(user, "localhost", Agent.CHROME, "XXXXXXXXX");
		} catch (AuthException e) {
			logger.info("{} {}", e.getStatus(), e.getMessage());
		}
	}
	
	@Test
	public void testGetUser() {
		User user = userService.getUser(oper, 1L);
		printUser(user);
		
		user = userService.getUser(oper, "jhat");
		printUser(user);
	}
	
	@Test
	public void testQueryUser() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "jhat");
//		query.addParameter("role_id", 1);
//		query.addParameter("role_name", "ADMIN");
		
		List<User> users = userService.getUsers(oper, query);
		printUser(users);
		
		logger.info("{}", userService.countUsers(oper, query));
	}
	
	@Test
	public void testIsUserUpdateable(){
		User user = userService.getUser(oper, 1L);
		logger.info("{}", userService.isUserUpdateable(oper, user));
	}
	
	@Test
	public void testIsUserDeleteable(){
		User user = userService.getUser(oper, 1L);
		logger.info("{}", userService.isUserDeleteable(oper, user));
	}
	
	@Test
	public void testIsUserLocked(){
		User user = userService.getUser(oper, 1L);
		logger.info("{}", userService.isUserLocked(oper, user));
	}
	
	@Test
	public void testLockUser(){
		User user = userService.getUser(oper, 1L);
		userService.lockUser(oper, user);
	}
	
	@Test
	public void testUnlockUser(){
		User user = userService.getUser(oper, 1L);
		userService.unlockUser(oper, user);
	}
	
	@Test
	public void testAddUserRole() throws AuthException {
		User user = userService.getUser(oper, "jhata");
		Role role = roleService.getRole(oper, "TestRole");
		logger.debug("{}", userService.addUserRole(oper, user, role));
	}
	
	@Test
	public void testHasUserRole() {
		User user = userService.getUser(oper, "jhata");
		Role role = roleService.getRole(oper, "TestRole");
		logger.debug("{}", userService.hasUserRole(oper, user, role));
	}
	
	@Test
	public void testIsRoleUpdateable(){
		User user = userService.getUser(oper, 1L);
		Role role = roleService.getRole(oper, 1L);
		logger.info("{}", userService.isUserRoleUpdateable(oper, user, role));
	}
	
	@Test
	public void testIsRoleDeleteable(){
		User user = userService.getUser(oper, 1L);
		Role role = roleService.getRole(oper, 1L);
		logger.info("{}", userService.isUserRoleDeleteable(oper, user, role));
	}
	
	@Test
	public void testIsRoleLocked(){
		User user = userService.getUser(oper, 1L);
		Role role = roleService.getRole(oper, 1L);
		logger.info("{}", userService.isUserRoleLocked(oper, user, role));
	}
	
	@Test
	public void testLockRole(){
		User user = userService.getUser(oper, 1L);
		Role role = roleService.getRole(oper, 1L);
		userService.lockUserRole(oper, user, role);
	}
	
	@Test
	public void testUnlockRole(){
		User user = userService.getUser(oper, 1L);
		Role role = roleService.getRole(oper, 1L);
		userService.unlockUserRole(oper, user, role);
	}
	
	@Test
	public void testRemoveUserRole() {
		User user = userService.getUser(oper, "jhata");
		Role role = roleService.getRole(oper, "TestRole");
		// logger.debug(userService.removeRole(user, role));
		userService.deleteUserRole(oper, user, role);
	}
	
	@Test
	public void testRemoveUser() {
		User user = userService.getUser(oper, "jhata");
		// userService.removeUser(user);
		userService.deleteUser(oper, user);
	}
	
}
