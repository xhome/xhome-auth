package org.xhome.xauth.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @project auth
 * @author xhome
 * @email cpf624@126.com
 * @date Jan 3, 20131:16:49 PM
 */
public abstract class AbstractTest {
	
	protected ApplicationContext	context	= null;
	protected Logger				logger	= LoggerFactory.getLogger(this.getClass());
	
	public AbstractTest() {
		context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	}
	
	protected void printUser(List<User> users) {
		if (users != null) {
			for (User user : users) {
				printUser(user);
			}
		}
	}
	
	protected void printUser(User user) {
		logger.debug("Id:" + user.getId() + "\tName:" + user.getName()
				+ "\tNick:" + user.getNick() + "\tEmail:" + user.getEmail()
				+ "\tStatus:" + user.getStatus());
		List<Role> roles = user.getRoles();
		printRole(roles);
	}
	
	protected void printRole(List<Role> roles) {
		if (roles != null) {
			for (Role role : roles) {
				printRole(role);
			}
		}
	}
	
	protected void printRole(Role role) {
		logger.debug("Id:" + role.getId() + "\tName:" + role.getName()
				+ "\tTip:" + role.getTip());
	}
	
//	protected void printLoginLog(List<LoginLog> loginLogs) {
//		if (loginLogs != null) {
//			for (LoginLog loginLog : loginLogs) {
//				printLoginLog(loginLog);
//			}
//		}
//	}
//	
//	protected void printLoginLog(LoginLog loginLog) {
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		logger.debug("Id:" + loginLog.getId() + "\tUser:" + loginLog.getUser()
//				+ "\tLogin:" + format.format(loginLog.getLogin())
//				+ "\tAddress:" + loginLog.getAddress() + "\tType:"
//				+ loginLog.getType() + "\tStatus:" + loginLog.getStatus());
//	}
	
}
