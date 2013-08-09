package org.xhome.xauth.core.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthException;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.AbstractTest;

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
	}
	
	@Test
	public void testAddUser() {
		try {
			User user = new User("jhatb", "abcdef");
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
			userService.addUser(user);
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	@Test
	public void testLogin() {
		User user = new User("jhata", "abcdef");
		try {
			userService.login(user);
		} catch (AuthException e) {
			logger.info("{}", e.getStatus());
		}
	}
	
	@Test
	public void testGetUser() {
		User user = userService.getUser(1L);
		printUser(user);
		
		user = userService.getUser("jhat");
		printUser(user);
	}
	
	@Test
	public void testQueryUser() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "jhat");
//		query.addParameter("role_id", 1);
//		query.addParameter("role_name", "ADMIN");
		
		List<User> users = userService.getUsers(query);
		printUser(users);
		
		logger.info("{}", userService.countUsers(query));
	}
	
	@Test
	public void testIsUserUpdateable(){
		User user = userService.getUser(1L);
		logger.info("{}", userService.isUserUpdateable(user));
	}
	
	@Test
	public void testIsUserDeleteable(){
		User user = userService.getUser(1L);
		logger.info("{}", userService.isUserDeleteable(user));
	}
	
	@Test
	public void testIsUserLocked(){
		User user = userService.getUser(1L);
		logger.info("{}", userService.isUserLocked(user));
	}
	
	@Test
	public void testLockUser(){
		User user = userService.getUser(1L);
		userService.lockUser(user);
	}
	
	@Test
	public void testUnlockUser(){
		User user = userService.getUser(1L);
		userService.unlockUser(user);
	}
	
	@Test
	public void testAddUserRole() throws AuthException {
		User user = userService.getUser("jhata");
		Role role = roleService.getRole("TestRole");
		logger.debug("{}", userService.addUserRole(user, role));
	}
	
	@Test
	public void testHasUserRole() {
		User user = userService.getUser("jhata");
		Role role = roleService.getRole("TestRole");
		logger.debug("{}", userService.hasUserRole(user, role));
	}
	
	@Test
	public void testIsRoleUpdateable(){
		User user = userService.getUser(1L);
		Role role = roleService.getRole(1L);
		logger.info("{}", userService.isUserRoleUpdateable(user, role));
	}
	
	@Test
	public void testIsRoleDeleteable(){
		User user = userService.getUser(1L);
		Role role = roleService.getRole(1L);
		logger.info("{}", userService.isUserRoleDeleteable(user, role));
	}
	
	@Test
	public void testIsRoleLocked(){
		User user = userService.getUser(1L);
		Role role = roleService.getRole(1L);
		logger.info("{}", userService.isUserRoleLocked(user, role));
	}
	
	@Test
	public void testLockRole(){
		User user = userService.getUser(1L);
		Role role = roleService.getRole(1L);
		userService.lockUserRole(user, role);
	}
	
	@Test
	public void testUnlockRole(){
		User user = userService.getUser(1L);
		Role role = roleService.getRole(1L);
		userService.unlockUserRole(user, role);
	}
	
	@Test
	public void testRemoveUserRole() {
		User user = userService.getUser("jhata");
		Role role = roleService.getRole("TestRole");
		// logger.debug(userService.removeRole(user, role));
		userService.deleteUserRole(user, role);
	}
	
	@Test
	public void testRemoveUser() {
		User user = userService.getUser("jhata");
		// userService.removeUser(user);
		userService.deleteUser(user);
	}
	
}
