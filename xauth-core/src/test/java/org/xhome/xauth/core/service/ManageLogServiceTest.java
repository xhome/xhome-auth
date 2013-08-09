package org.xhome.xauth.core.service;

import java.util.List;
import org.junit.Test;
import org.xhome.common.constant.Action;
import org.xhome.common.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.core.AbstractTest;

/**
 * @project auth
 * @author jhat
 * @email cpf624@126.com
 * @date Feb 1, 201310:49:01 AM
 */
public class ManageLogServiceTest extends AbstractTest {
	
	private ManageLogService	manageLogService;
	
	public ManageLogServiceTest() {
		manageLogService = context.getBean(ManageLogServiceImpl.class);
	}
	
	@Test
	public void testAddManageLog() {
		ManageLog manageLog = new ManageLog(Action.ADD, ManageLog.TYPE_AUTH_LOG, 10L, 1L);
		manageLogService.logManage(manageLog);
	}
	
	@Test
	public void testGetManageLogs() {
		List<ManageLog> manageLogs = manageLogService.getManageLogs();
		printManageLog(manageLogs);
		
		QueryBase query = new QueryBase();
//		query.addParameter("status", "0");
		manageLogs = manageLogService.getManageLogs(query);
		printManageLog(manageLogs);
	}
	
}