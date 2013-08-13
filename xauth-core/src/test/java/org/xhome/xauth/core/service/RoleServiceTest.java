package org.xhome.xauth.core.service;

import java.util.List;
import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xauth.core.AbstractTest;
import org.xhome.xauth.core.listener.TestRoleManageListener;

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
		oper.setId(101L);
		
		((RoleServiceImpl)roleService).registerRoleManageListener(new TestRoleManageListener());
	}
	
	@Test
	public void testAddRole() {
		Role role = new Role("TestRole");
		role.setOwner(1L);
		role.setModifier(1L);
		roleService.addRole(oper, role);
	}
	
	@Test
	public void testGetRole() {
		Role role = roleService.getRole(oper, 1L);
		printRole(role);
		
		role = roleService.getRole(oper, "TestRole");
		printRole(role);
	}
	
	@Test
	public void testCountRoles() {
		logger.info("{}", roleService.countRoles(oper));
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", roleService.countRoles(oper, query));
	}
	
	@Test
	public void testGetRoles() {
		List<Role> roles = roleService.getRoles(oper);
		printRole(roles);
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		roles = roleService.getRoles(oper, query);
		printRole(roles);
	}
	
	@Test
	public void testIsRoleUpdateable(){
		Role role = roleService.getRole(oper, 1L);
		logger.info("{}", roleService.isRoleUpdateable(oper, role));
	}
	
	@Test
	public void testIsRoleDeleteable(){
		Role role = roleService.getRole(oper, 1L);
		logger.info("{}", roleService.isRoleDeleteable(oper, role));
	}
	
	@Test
	public void testIsRoleLocked(){
		Role role = roleService.getRole(oper, 1L);
		logger.info("{}", roleService.isRoleLocked(oper, role));
	}
	
	@Test
	public void testLockRole(){
		Role role = roleService.getRole(oper, 1L);
		roleService.lockRole(oper, role);
	}
	
	@Test
	public void testUnlockRole(){
		Role role = roleService.getRole(oper, 1L);
		roleService.unlockRole(oper, role);
	}
	
	@Test
	public void testUpdateRole() {
		Role role = roleService.getRole(oper, "TestRole");
		role.setTip(1L);
		role.setId(100L);
		// role.setVersion(11);
		int r = roleService.updateRole(oper, role);
		logger.info("result:" + r);
	}
	
	@Test
	public void testIsRoleExists() {
		logger.info("{}", roleService.isRoleExists(oper, new Role("TestRole")));
	}
	
	@Test
	public void testRemoveRole() {
		Role role = roleService.getRole(oper, "TestRole");
		roleService.removeRole(oper, role);
		roleService.deleteRole(oper, role);
	}
	
}
