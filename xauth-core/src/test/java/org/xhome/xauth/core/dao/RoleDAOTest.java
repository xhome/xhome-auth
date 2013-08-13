package org.xhome.xauth.core.dao;

import java.util.List;
import org.xhome.xauth.Role;
import org.xhome.xauth.core.AbstractTest;
import org.xhome.xauth.core.dao.RoleDAO;
import org.xhome.db.query.QueryBase;
import org.junit.Test;

/**
 * @project auth
 * @author xhome
 * @email cpf624@126.com
 * @date Jan 3, 20131:07:38 PM
 */
public class RoleDAOTest extends AbstractTest {
	
	private RoleDAO	roleDAO;
	
	public RoleDAOTest() {
		roleDAO = context.getBean("roleDAO", RoleDAO.class);
	}
	
	@Test
	public void testAddRole() {
		logger.debug("test add role");
		
		Role role = new Role("Role");
		role.setOwner(1L);
		role.setModifier(1L);
		
		roleDAO.addRole(role);
		
	}
	
	@Test
	public void testUpdateRole() {
		logger.debug("test update role");
		
		Role role = roleDAO.queryRoleById(2L);
		printRole(role);
		role.setVersion((short)3);
		
		roleDAO.updateRole(role);
		
		role = roleDAO.queryRoleById(2L);
		printRole(role);
	}
	
	@Test
	public void testQueryRole() {
		logger.debug("test query role by id");
		
		Role role = roleDAO.queryRoleById(1L);
		printRole(role);
		
		logger.debug("test query role by name");
		
		role = roleDAO.queryRoleByName("ADMIN");
		printRole(role);
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "A");
		List<Role> roles = roleDAO.queryRoles(query);
		printRole(roles);
		
	}
	
}
