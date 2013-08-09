package org.xhome.xauth.core.dao;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.xhome.common.constant.Agent;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.AbstractTest;

/**
 * @project auth
 * @author jhat
 * @email cpf624@126.com
 * @date Feb 1, 201310:17:16 AM
 */
public class AuthLogDAOTest extends AbstractTest {
	
	private AuthLogDAO	authLogDAO;
	
	public AuthLogDAOTest() {
		authLogDAO = context.getBean(AuthLogDAO.class);
	}
	
	@Test
	public void testAddAuthLog() {
		User user = new User("jhat");
		AuthLog authLog = new AuthLog(user, "MD5", "fe80ca3a35fffecf", Agent.CHROME, "chrome 20.01 ubuntu 64bit");
		authLog.setCreated(new Timestamp(System.currentTimeMillis()));
		authLogDAO.addAuthLog(authLog);
	}
	
	@Test
	public void testQueryAuthLog() {
		QueryBase query = new QueryBase();
		List<AuthLog> authLogs = authLogDAO.queryAuthLogs(query);
		printAuthLog(authLogs);
	}
	
}
