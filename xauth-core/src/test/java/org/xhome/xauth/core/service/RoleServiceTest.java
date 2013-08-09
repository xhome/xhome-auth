package org.xhome.xauth.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xauth.core.AbstractTest;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 20131:33:15 PM
 */
public class RoleServiceTest extends AbstractTest {
	
	private RoleService	roleService;
	
	public RoleServiceTest() {
		roleService = context.getBean(RoleServiceImpl.class);
	}
	
	@Test
	public void testAddRole() {
		Role role = new Role("TestRole");
		role.setOwner(1L);
		role.setModifier(1L);
		roleService.addRole(role);
	}
	
	@Test
	public void testGetRole() {
		Role role = roleService.getRole(1L);
		printRole(role);
		
		role = roleService.getRole("TestRole");
		printRole(role);
	}
	
	@Test
	public void testCountRoles() {
		logger.info("{}", roleService.countRoles());
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", roleService.countRoles(query));
	}
	
	@Test
	public void testGetRoles() {
		List<Role> roles = roleService.getRoles();
		printRole(roles);
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		roles = roleService.getRoles(query);
		printRole(roles);
	}
	
	@Test
	public void testIsRoleUpdateable(){
		Role role = roleService.getRole(1L);
		logger.info("{}", roleService.isRoleUpdateable(role));
	}
	
	@Test
	public void testIsRoleDeleteable(){
		Role role = roleService.getRole(1L);
		logger.info("{}", roleService.isRoleDeleteable(role));
	}
	
	@Test
	public void testIsRoleLocked(){
		Role role = roleService.getRole(1L);
		logger.info("{}", roleService.isRoleLocked(role));
	}
	
	@Test
	public void testLockRole(){
		Role role = roleService.getRole(1L);
		roleService.lockRole(role);
	}
	
	@Test
	public void testUnlockRole(){
		Role role = roleService.getRole(1L);
		roleService.unlockRole(role);
	}
	
	@Test
	public void testUpdateRole() {
		Role role = roleService.getRole("TestRole");
		role.setTip(1L);
		role.setId(100L);
		// role.setVersion(11);
		int r = roleService.updateRole(role);
		logger.info("result:" + r);
	}
	
	@Test
	public void testIsRoleExists() {
		logger.info("{}", roleService.isRoleExists(new Role("TestRole")));
	}
	
	@Test
	public void testRemoveRole() {
		Role role = roleService.getRole("TestRole");
		roleService.removeRole(role);
		roleService.deleteRole(role);
	}
	
}