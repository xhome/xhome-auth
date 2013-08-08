package org.xhome.xauth.core.dao;

import java.util.List;
import org.xhome.xauth.User;
import org.xhome.xauth.core.AbstractTest;
import org.xhome.xauth.core.dao.UserDAO;
import org.xhome.common.query.QueryBase;
import org.junit.Test;

/**
 * @project auth
 * @author xhome
 * @email cpf624@126.com
 * @date Jan 3, 20131:07:38 PM
 */
public class UserDAOTest extends AbstractTest {
	
	private UserDAO	userDAO;
	
	public UserDAOTest() {
		userDAO = context.getBean("userDAO", UserDAO.class);
	}
	
	@Test
	public void testAddUser() {
		logger.debug("test add user");
		
		User user = new User("Tbc", "PASSWD");
		user.setNick("NICK");
		user.setEmail("cpf624@126.com");
		user.setOwner(1L);
		user.setModifier(1L);
		
		userDAO.addUser(user);
		
	}
	
	@Test
	public void testUpdateUser() {
		logger.debug("test update User");
		
		User user = userDAO.queryUserById(2L);
		user.setVersion(2L);
		printUser(user);
		
		userDAO.updateUser(user);
		user = userDAO.queryUserById(2L);
		printUser(user);
	}
	
	@Test
	public void testQueryUser() {
		logger.debug("test query user by id");
		
		User user = userDAO.queryUserById(1L);
		printUser(user);
		
		logger.debug("test query user by name");
		
		user = userDAO.queryUserByName("xhome");
		printUser(user);
		
		QueryBase query = new QueryBase();
		List<User> users = userDAO.queryUsers(query);
		printUser(users);
		
	}
	
}
