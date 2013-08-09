package org.xhome.xauth.core.service;

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
 * @date Feb 1, 201310:49:01 AM
 */
public class AuthLogServiceTest extends AbstractTest {
	
	private AuthLogService	authLogService;
	
	public AuthLogServiceTest() {
		authLogService = context.getBean(AuthLogServiceImpl.class);
	}
	
	@Test
	public void testAddAuthLog() {
		User user = new User("jhat");
		AuthLog authLog = new AuthLog(user, "MD5", "fe80ca3a35fffecf", Agent.CHROME, "chrome 20.01 ubuntu 64bit");
		authLog.setCreated(new Timestamp(System.currentTimeMillis()));
		authLogService.logAuth(authLog);
	}
	
	@Test
	public void testGetAuthLogs() {
		List<AuthLog> authLogs = authLogService.getAuthLogs();
		printAuthLog(authLogs);
		
		QueryBase query = new QueryBase();
		query.addParameter("user", "jhat");
		query.addParameter("auth", "2013-");
		query.addParameter("address", "192");
		query.addParameter("type", "0");
		query.addParameter("status", "0");
		authLogs = authLogService.getAuthLogs(query);
		printAuthLog(authLogs);
	}
	
}
