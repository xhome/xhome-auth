package org.xhome.xauth.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xhome.xauth.AuthLog;
import org.xhome.xauth.Config;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Jan 3, 20131:16:49 PM
 */
public abstract class AbstractTest {

	protected ApplicationContext context = null;
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected User oper;

	public AbstractTest() {
		context = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml");
		oper = new User("jhat");
	}

	protected void printUser(List<User> users) {
		if (users != null) {
			for (User user : users) {
				printUser(user);
			}
		}
	}

	protected void printUser(User user) {
		if (user == null) {
			return;
		}
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
		if (role == null) {
			return;
		}
		logger.debug("Id:" + role.getId() + "\tName:" + role.getName());
	}

	protected void printConfig(List<Config> configs) {
		if (configs != null) {
			for (Config config : configs) {
				printConfig(config);
			}
		}
	}

	protected void printConfig(Config config) {
		if (config == null) {
			return;
		}
		logger.debug("Id:" + config.getId() + "\tCategory:"
				+ config.getCategory() + "\tItem:" + config.getItem()
				+ "\tDisplay:" + config.getDisplay() + "\tValue:"
				+ config.getValue());
	}

	protected void printAuthLog(List<AuthLog> authLogs) {
		if (authLogs != null) {
			for (AuthLog authLog : authLogs) {
				printAuthLog(authLog);
			}
		}
	}

	protected void printAuthLog(AuthLog authLog) {
		if (authLog == null) {
			return;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.debug("Id:" + authLog.getId() + "\tUser:"
				+ authLog.getUser().getName() + "\tTime:"
				+ format.format(authLog.getCreated()) + "\tMethod:"
				+ authLog.getMethod() + "\tAddress:" + authLog.getAddress()
				+ "\tAgent:" + authLog.getAgent() + "\tNumber:"
				+ authLog.getNumber() + "\tStatus:" + authLog.getStatus());
	}

	protected void printManageLog(List<ManageLog> manageLogs) {
		if (manageLogs != null) {
			for (ManageLog manageLog : manageLogs) {
				printManageLog(manageLog);
			}
		}
	}

	protected void printManageLog(ManageLog manageLog) {
		if (manageLog == null) {
			return;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.debug("Id:" + manageLog.getId() + "\tAction:"
				+ manageLog.getAction() + "\tType:" + manageLog.getType()
				+ "\tObj:" + manageLog.getObj() + "\tTime:"
				+ format.format(manageLog.getCreated()) + "\tStatus:"
				+ manageLog.getStatus());
	}

}
